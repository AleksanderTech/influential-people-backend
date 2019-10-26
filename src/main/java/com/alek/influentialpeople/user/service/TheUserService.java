package com.alek.influentialpeople.user.service;

import com.alek.influentialpeople.common.TwoWayConverter;
import com.alek.influentialpeople.exception.ExceptionMessages;
import com.alek.influentialpeople.exception.exceptions.StateConflictException;
import com.alek.influentialpeople.user.entity.User;
import com.alek.influentialpeople.user.model.UserResponse;
import com.alek.influentialpeople.user.persistence.UserRepository;
import com.alek.influentialpeople.user.role.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class TheUserService implements UserService {

    private UserRepository userRepository;
    private TwoWayConverter<User, UserResponse> converter;
    private PasswordEncoder passwordEncoder;
    private CurrentUserHolder userHolder;

    public TheUserService(UserRepository userRepository, TwoWayConverter<User, UserResponse> converter, PasswordEncoder passwordEncoder, CurrentUserHolder userHolder) {
        this.userRepository = userRepository;
        this.converter = converter;
        this.passwordEncoder = passwordEncoder;
        this.userHolder = userHolder;
    }

    @Override
    public Page<UserResponse> findAll(Pageable pageable) {
        Page<User> page = userRepository.findAll(pageable);
        return new PageImpl(page.getContent().stream().map(user -> converter.convert(user)).collect(Collectors.toList()), pageable, page.getTotalElements());
    }

    @Override
    public UserResponse findUser(String username, boolean inSecureWay) {

        User user = userRepository.findById(username).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException(ExceptionMessages.NOT_FOUND_USER_MESSAGE);
        }
        if (inSecureWay) {
            if (isAllowed(username)) {
                return converter.convert(userRepository.findById(username).get());
            }
        }
        return converter.convert(user);
    }

    @Override
    public void deleteUser(String username, boolean inSecureWay) {

        User user = userRepository.findById(username).orElse(null);
        if (inSecureWay) {
            if (user.getUsername().equals(username)) {
                throw new StateConflictException(ExceptionMessages.STATE_CONFLICT_MESSAGE);
            }
            if (user == null) {
                throw new UsernameNotFoundException(ExceptionMessages.NOT_FOUND_USER_MESSAGE);
            }
        }
        userRepository.deleteById(username);
    }

    @Override
    public UserResponse createUser(User user, boolean inSecureWay) {
        if (!inSecureWay) {
            user.setEnabled(true);
        } else {
            user.setEnabled(false);
            user.setRoles(new HashSet(Arrays.asList(new Role(Role.Roles.ROLE_USER))));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return converter.convert(userRepository.save(user));
    }

    private boolean isAllowed(String username) {
        User user = userHolder.getUser();
        return user.getUsername().equals(username) || userHolder.isUserAdmin(user);
    }
}

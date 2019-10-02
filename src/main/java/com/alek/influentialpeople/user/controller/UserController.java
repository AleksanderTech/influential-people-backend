package com.alek.influentialpeople.user.controller;

import com.alek.influentialpeople.user.controller.dto.UserResponseDto;
import com.alek.influentialpeople.user.domain.User;
import com.alek.influentialpeople.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService theUserService;

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public Page<UserResponseDto> findAll(Pageable pageable) {

        return theUserService.findAll(pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(path = "/{username}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@PathVariable String username) {

        theUserService.deleteUser(username);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @RequestMapping(path = "/{username}", method = RequestMethod.GET)
    public UserResponseDto findUser(@PathVariable String username) {

        return new UserResponseDto(theUserService.findUser(username));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public User createUser(@RequestBody User user) {

        return theUserService.createUser(user);
    }
}

package com.alek.influentialpeople.security.controller;

import com.alek.influentialpeople.TestUtils;
import com.alek.influentialpeople.common.ConvertersFactory;
import com.alek.influentialpeople.common.TwoWayConverter;
import com.alek.influentialpeople.exception.controller.ExceptionController;
import com.alek.influentialpeople.security.model.UserRegistration;
import com.alek.influentialpeople.security.service.AuthService;
import com.alek.influentialpeople.user.entity.User;
import com.alek.influentialpeople.user.role.entity.Role;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.stream.Collectors;

import static com.alek.influentialpeople.common.ConvertersFactory.getConverter;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class AuthControllerTest {

    private MockMvc mockMvc;
    private User user;

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Before
    public void setUp() {
        user = User.builder().username("user1").email("email1@email.com").roles(Sets.newHashSet(new Role(Role.Roles.ROLE_USER))).build();
        mockMvc = MockMvcBuilders.standaloneSetup(authController).setControllerAdvice(new ExceptionController()).build();
    }

    @Test
    public void signUp_userDoesNotExists_shouldReturnStatus201() throws Exception {

        TwoWayConverter<UserRegistration, User> converterR = getConverter(ConvertersFactory.ConverterType.USER_REGISTRATION_TO_USER);

        Mockito.when(authService.signUp(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/sign-up").contentType(APPLICATION_JSON_UTF8)
                .content(TestUtils.stringify(converterR.convertBack(user))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.roles").value(user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList())));
    }

    @Test
    public void confirm_properToken_shouldRedirect() throws Exception {

        String verificationToken="token";

        Mockito.when(authService.confirm(any(String.class))).thenReturn(verificationToken);

        mockMvc.perform(get("/confirm?token="+verificationToken).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isFound());
    }
}
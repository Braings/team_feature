package com.bridgeX;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bridgeX.user.dto.LoginRequest;
import com.bridgeX.user.dto.LoginResponse;
import com.bridgeX.user.service.UserService;

// API Test
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void testLogin_T() {
        LoginRequest request = new LoginRequest();
        request.setUsername("Potato");
        request.setPassword("1234");

        LoginResponse response = userService.login(request);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("로그인 성공");
    }
    /*
    @Test
    void testLogin_F() {
        LoginRequest request = new LoginRequest();
        request.setUsername("Potato");
        request.setPassword("1234");

        LoginResponse response = userService.login(request);

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).isEqualTo("Login Failed");        
    }
    */
}

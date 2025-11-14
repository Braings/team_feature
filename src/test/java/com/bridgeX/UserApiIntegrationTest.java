package com.bridgeX;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) //임시로 비활성화
public class UserApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void signup_and_login() throws Exception {

        // 1) 회원가입 요청 JSON  (/api/sign)
        String signupJson = """
            {
              "username": "TestUser",
              "email": "test1234@test.com",
              "password": "1234"
            }
            """;

        mockMvc.perform(
                post("/api/sign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupJson)
        )
        .andExpect(status().isOk()); // 회원가입이 OK(200) 나오는지만 확인

        // 2) 로그인 요청 JSON  (/api/login)
        String loginJson = """
            {
              "username": "TestUser",
              "password": "1234"
            }
            """;

        mockMvc.perform(
                post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("로그인 성공")); // UserService와 string 맞추기
    }
}

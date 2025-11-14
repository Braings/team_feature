package com.bridgeX;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    
    // Test02: User name Duplication
    
    @Test
    void Username_Duplication() throws Exception {
    	// first Register
    	/*
    	String signupJson1 = """
    	        {
    	          "username": "DuplicateUser",
    	          "email": "test1@test.com",
    	          "password": "1234"
    	        }
    	        """;

    	    mockMvc.perform(
    	            post("/api/sign")
    	                    .contentType(MediaType.APPLICATION_JSON)
    	                    .content(signupJson1)
    	    )
    	    .andExpect(status().isOk());
    	*/
    	    // second Register (Duplicate)
        String signupJson2 = """
            {
              "username": "DuplicateUser",
              "email": "test2@test.com",
              "password": "5678"
            }
            """;

        mockMvc.perform(
                post("/api/sign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupJson2)
        )
        .andExpect(status().isBadRequest()) // Expect 400
        .andExpect(jsonPath("$.message").value("이미 존재하는 아이디입니다.")); // UserService와 반환 string을 맞춰야 함
    }

    
    
    /*
    
    // Test01: User Register and Login
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
    */
}

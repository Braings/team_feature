package com.bridgeX;


import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotEquals;
// import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.bridgeX.user.domain.SiteUser;
import com.bridgeX.user.domain.UserGender;
import com.bridgeX.user.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) //임시로 Spring Security 비활성화
public class UserApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private UserRepository userRepository;

    // Test00: New User Register and Confirm
    @Test
    void signup_and_verify_saved_info() throws Exception {

        // 1) Sign-up
        String signupJson = """
        {
            "nickname": "Im_Test_User3",
            "username": "TestUser03",
            "email": "tu3@test.com",
            "password": "12345678a!",
            "sex": "FEMALE",
            "birthday": "2003-12-25",
            "height": "150",
            "weight": "45"
        }
        """;

        mockMvc.perform(
                    post("/api/sign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupJson)
                )
                .andExpect(status().isOk()); // Success Sign-up

        // 2) Check from DB
        SiteUser savedUser = userRepository.findByUsername("TestUser03")
                .orElseThrow(() -> new RuntimeException("회원가입 후 DB에서 유저를 찾지 못했습니다."));

        // 3) Confirm Data
        assertEquals("Im_Test_User3", savedUser.getNickname());
        assertEquals("TestUser03", savedUser.getUsername());
        assertEquals("tu3@test.com", savedUser.getEmail());
        assertEquals(UserGender.FEMALE, savedUser.getBodyInfo().getGender());
        assertEquals(LocalDate.of(2003, 12, 25), savedUser.getBirthday());
        assertEquals(150, savedUser.getBodyInfo().getHeight());
        assertEquals(45, savedUser.getBodyInfo().getWeight());
    }
    
    /*
    // Test01: User Register and Login test
    @Test
    void signup_and_login() throws Exception {
        // 1) 회원가입 요청 JSON  (/api/sign)
        String signupJson = """
            {
              "username": "Im_Test_User",
              "email": "PlzSuccess@test.com",
              "password": "1234",
              "passwordCheck": "1234",
              "birthday": "1685-02-29"
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
              "username": "Im_Test_User",
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
    
    /*
    // Test02: Duplication User Signup test
    
    @Test
    void Username_Duplication() throws Exception {
    	
    	
    	String signupJson1 = """
    	        {
    	          "username": "Potato",
    	          "email": "test1@test.com",
    	          "password": "9999"
    	        }
    	        """;

    	    mockMvc.perform(
    	            post("/api/sign")
    	                    .contentType(MediaType.APPLICATION_JSON)
    	                    .content(signupJson1)
    	    ).andExpect(status().isOk());
    	
    	
    	String signupJson2 = """
    	        {
    	          "username": "Potato2",
    	          "email": "test1@test.com",
    	          "password": "9999"
    	        }
    	        """;

    	    mockMvc.perform(
    	            post("/api/sign")
    	                    .contentType(MediaType.APPLICATION_JSON)
    	                    .content(signupJson2)
    	    )
    	    .andExpect(status().isBadRequest())
    	    .andExpect(content().string("이미 등록된 이메일입니다."));
    }
    */
    
    /*
    // Test03: Wrong format Signup test
    
    @Test
    void Wrong_format_signup() throws Exception {
    	String signupJson = """
    	        {
    	          "username": "apple",
    	          "email": "ILoveApple@fruit.com",
    	          "password": ""
    	        }
    	        """;

    	    mockMvc.perform(
    	            post("/api/sign")
    	                    .contentType(MediaType.APPLICATION_JSON)
    	                    .content(signupJson)
    	    )
    	    .andExpect(status().isBadRequest())
    	    .andExpect(content().string("비밀번호는 공백으로 둘 수 없습니다."));
    }
 	*/
    
    /*
    // Test04: Empty format Login test
    
    @Test
    void Empty_format_signup() throws Exception {
    	String loginJson = """
    	        {
    	          "username": "TestUser",
    	          "password": ""
    	        }
    	        """;

    	    mockMvc.perform(
    	            post("/api/login")
    	                    .contentType(MediaType.APPLICATION_JSON)
    	                    .content(loginJson)
    	    )
    	    .andExpect(status().isBadRequest())
    	    .andExpect(content().string("비밀번호는 공백으로 둘 수 없습니다."));
    }
	*/
    
    /*
    // Test05: Bad request: login
    
    @Test
    void Bad_login_request() throws Exception {
    	String loginJson = """
    	        {
    	          "username": "PotatoLuver",
    	          "password": "1239"
    	        }
    	        """;

    	    mockMvc.perform(
    	            post("/api/login")
    	                    .contentType(MediaType.APPLICATION_JSON)
    	                    .content(loginJson)
    	    )
    	    .andExpect(status().isBadRequest())
    	    .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").value("존재하지 않는 아이디입니다."));
    }
	*/
    
    /*
    // Test06: Signup - incorrect password Check
    
    @Test
    void PasswordCheck() throws Exception {
    	String SignupJson = """
    	        {
    	          "username": "NANANANA",
    	          "email": "LALALALA@lala.com",
    	          "password": "12345",
    	          "passwordCheck": "12345677"
    	        }
    	        """;

    	mockMvc.perform(
	            post("/api/sign")
	                    .contentType(MediaType.APPLICATION_JSON)
	                    .content(SignupJson)
	    )
	    .andExpect(status().isBadRequest())
	    .andExpect(content().string("비밀번호가 일치하지 않습니다."));
    }
	*/
}

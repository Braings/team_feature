package com.bridgeX;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.bridgeX.forum.dto.ForumPostCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser
class ForumApiIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    
    // Test01: Post Content
    
    @Test
    void NewPost() throws Exception {
	
//    	String NewContentJson = """
//    	        {
//    	          "title": "This is title",
//    	          "content": "The title is about the content",
//    	          "author": "Potato"
//    	        }
//    	        """;
    	
    	// Use DTO
    	ForumPostCreateRequest request = new ForumPostCreateRequest();
        request.setTitle("This is title");
        request.setContent("The title is about the content");
        request.setAuthor("Potato");

        String NewContentJson = objectMapper.writeValueAsString(request);
    	
        mockMvc.perform(
                post("/api/forum/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(NewContentJson)
        )
        .andExpect(status().isOk())           // check if 200
        .andExpect(jsonPath("$.id").exists()) // DB check
        .andExpect(jsonPath("$.title").value("This is title"))
        .andExpect(jsonPath("$.author").value("Potato"))
        .andDo(print());
	}
}

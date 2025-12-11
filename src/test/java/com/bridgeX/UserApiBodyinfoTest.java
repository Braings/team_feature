package com.bridgeX;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertTrue;

// import java.time.LocalDate;

// import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.test.web.servlet.MockMvc;

// import com.bridgeX.user.domain.SiteUser;
// import com.bridgeX.user.domain.SiteUserBody;
// import com.bridgeX.user.domain.UserGender;
// import com.bridgeX.user.dto.UserInfoModifyRequest;
import com.bridgeX.user.repository.UserRepository;
import com.bridgeX.user.service.UserService;

// import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
// @Transactional
public class UserApiBodyinfoTest {
	// @Autowired
	// private MockMvc mockMvc;

	// TEST02 : User Info Modify
	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	/*
	@Test
	void modifyUserInfo() {
		// input Info to DB
		SiteUserBody body = SiteUserBody.builder()
				.height(170)
				.weight(60)
				.gender(UserGender.MALE)
				.build();

		SiteUser user = SiteUser.builder()
				.username("user1")
				.nickname("oldNick")
				.password(passwordEncoder.encode("oldPw"))
				.email("old@example.com")
				.birthday(LocalDate.of(2000, 1, 1))
				.bodyInfo(body)
				.build();
		body.setUser(user);
		
		userRepository.save(user);

		// Modify DTO
		UserInfoModifyRequest dto = UserInfoModifyRequest.builder()
				.nickname("newNick")
				.password("newPw")
				.birthday(LocalDate.of(1999, 2, 2))
				.email("new@example.com")
				.sex(UserGender.FEMALE)
				.height(180)
				.weight(70)
				.build();

		userService.modifyUserInfo("user1", dto);
		
		// Confirm
		SiteUser changed = userRepository.findByUsername("user1")
				.orElseThrow();
		// Site User info
		assertEquals("newNick", changed.getNickname());
		assertEquals("new@example.com", changed.getEmail());
		assertEquals(LocalDate.of(1999, 2, 2), changed.getBirthday());
		assertTrue(passwordEncoder.matches("newPw", changed.getPassword()));
		
		// Site User Body info
		assertNotNull(changed.getBodyInfo());
		assertEquals(UserGender.FEMALE, changed.getBodyInfo().getGender());
		assertEquals(180, changed.getBodyInfo().getHeight());
		assertEquals(70, changed.getBodyInfo().getWeight());
		
	}
	*/
	
	/*
	// TEST01
	
	@Test
	@WithMockUser(username = "Potato", roles = "ADMIN")
	void updateAndGetBodyInfo_forPotato() throws Exception {

		String requestJson = """
				{
					"height": 168,
					"weight": 55,
					"gender": "MALE"
				}""";

		mockMvc.perform(put("/api/me/body")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
		.andExpect(status().isOk());
		
		mockMvc.perform(get("/api/me/body"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.height").value(168))
		.andExpect(jsonPath("$.weight").value(55))
		.andExpect(jsonPath("$.gender").value("MALE"));
		}
		*/
}

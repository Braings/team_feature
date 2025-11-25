package com.bridgeX.user.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgeX.DataNotFoundException;
import com.bridgeX.user.domain.SiteUser;
import com.bridgeX.user.domain.SiteUserBody;
import com.bridgeX.user.domain.UserRole;
import com.bridgeX.user.dto.LoginRequest;
import com.bridgeX.user.dto.LoginResponse;
import com.bridgeX.user.dto.SignupRequest;
import com.bridgeX.user.dto.UserBodyInfoRequest;
import com.bridgeX.user.dto.UserBodyInfoResponse;
import com.bridgeX.user.dto.UserInfoResponse;
import com.bridgeX.user.repository.UserBodyRepository;
import com.bridgeX.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	// RP code
    private final UserRepository userRepository;
    private final UserBodyRepository userBodyRepository;
    private final PasswordEncoder passwordEncoder;


    // Sign-up Request
    public void signup(SignupRequest dto) {
        // Check ID duplication.
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 등록된 사용자입니다.");
        }
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
        	throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }
        if (!dto.getPassword().equals(dto.getPasswordCheck())) {
        	throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        // create User
        SiteUser user = new SiteUser();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setBirthday(dto.getBirthday());
        
        // User Role - User
        user.setRole(UserRole.USER);

        userRepository.save(user);
    }


    // Login Request
    public LoginResponse login(LoginRequest dto) {
        LoginResponse response = new LoginResponse();

        // find User
        Optional<SiteUser> siteUserOpt = userRepository.findByUsername(dto.getUsername());

        if (siteUserOpt.isEmpty()) {
            response.setSuccess(false);
            response.setMessage("존재하지 않는 아이디입니다.");
            return response;
        }

        SiteUser user = siteUserOpt.get();

        // Check Password
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            response.setSuccess(false);
            response.setMessage("비밀번호가 일치하지 않습니다.");
            return response;
        }

        response.setSuccess(true);
        response.setMessage("로그인 성공");
        // TODO: 나중에 토큰, 만료시간 등도 여기에

        return response;
    }
	
    // User Information Response
    public UserInfoResponse getMyInfo(String username) {
    	
    	SiteUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));
        
        return new UserInfoResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getBirthday(),
                user.getRole().name()
        );
    }
    
    // User Body Information Request
    public void updateBodyInfo(String username, UserBodyInfoRequest dto) {
        SiteUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("유저가 없습니다."));

        // Not Exist BodyInfo -> Generation
        SiteUserBody body = userBodyRepository.findByUser(user)
                .orElseGet(() -> {
                    SiteUserBody newBody = new SiteUserBody();
                    newBody.setUser(user);
                    return newBody;
                });

        body.setHeight(dto.getHeight());
        body.setWeight(dto.getWeight());
        body.setGender(dto.getGender());

        userBodyRepository.save(body);
    }

    
    // User Body Information Response
    public UserBodyInfoResponse getBodyInfo(String username) {
    	
    	SiteUserBody user = userBodyRepository.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));
        
        return new UserBodyInfoResponse(
                user.getHeight(),
                user.getWeight(),
                user.getGender().name()
        );
    }
    
    
    
	// Back-end Test only.
    // Dummy Function
	public SiteUser create(String username, String email, String password) {
		SiteUser user = new SiteUser();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		this.userRepository.save(user);
		return user;
	}

	public SiteUser getUser(String username) {
		Optional<SiteUser> siteUser = this.userRepository.findByUsername(username);
		if (siteUser.isPresent()) {
			return siteUser.get();
		} else {
			throw new DataNotFoundException("유저가 존재하지 않습니다.");
		}
	}
}

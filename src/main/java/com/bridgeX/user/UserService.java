package com.bridgeX.user;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgeX.DataNotFoundException;
import com.bridgeX.user.dto.LoginRequest;
import com.bridgeX.user.dto.LoginResponse;
import com.bridgeX.user.dto.SignupRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	// RP code
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // Sign-up
    public void signup(SignupRequest dto) {
        // Check ID duplication.
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 등록된 사용자입니다.");
        }
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
        	throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }
        // create User
        SiteUser user = new SiteUser();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));


        userRepository.save(user);
    }


    // Login
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
	
	
	// Back-end Test only.
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
			throw new DataNotFoundException("siteuser not found");
		}
	}
}

package com.bridgeX.user.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.bridgeX.user.domain.SiteUser;
import com.bridgeX.user.domain.SiteUserBody;
import com.bridgeX.user.domain.UserRole;
import com.bridgeX.user.dto.LoginRequest;
import com.bridgeX.user.dto.LoginResponse;
import com.bridgeX.user.dto.SignupRequest;
import com.bridgeX.user.dto.UserBodyInfoRequest;
import com.bridgeX.user.dto.UserBodyInfoResponse;
import com.bridgeX.user.dto.UserInfoModifyRequest;
import com.bridgeX.user.dto.UserInfoResponse;
import com.bridgeX.user.exception.CustomException;
import com.bridgeX.user.exception.ErrorCode;
import com.bridgeX.user.repository.UserBodyRepository;
import com.bridgeX.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	// RP code
    private final UserRepository userRepository;
    private final UserBodyRepository userBodyRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    

    // Sign-up Request
    public void signup(SignupRequest dto) {
        // Check ID duplication.
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("[SERVER] 이미 등록된 사용자입니다.");
        }
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
        	throw new IllegalArgumentException("[SERVER] 이미 등록된 이메일입니다.");
        }
        if (userRepository.findByNickname(dto.getNickname()).isPresent()) {
        	throw new IllegalArgumentException("[SERVER] 중복되는 닉네임입니다.");
        }
        // create User
        SiteUser user = SiteUser.builder()
        		.nickname(dto.getNickname())
        		.username(dto.getUsername())
        		.password(passwordEncoder.encode(dto.getPassword()))
        		.email(dto.getEmail())
        		.birthday(dto.getBirthday())
        		.role(UserRole.USER) // Default user role - User
        		.build();

        SiteUserBody body = SiteUserBody.builder()
        		.height(dto.getHeight())
        		.weight(dto.getWeight())
        		.gender(dto.getSex())
        		.user(user)
        		.build();
        
        userRepository.save(user);
        userBodyRepository.save(body);
    }
    
    
    // Login Request
    public LoginResponse login(LoginRequest dto) {
        LoginResponse response = new LoginResponse();

        try {
            // 1. 시큐리티에게 인증 시키기
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());

            Authentication authentication = authenticationManager.authenticate(authToken);

            // 2. 인증 성공했으면 SecurityContext 에 넣기
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 3. 세션 생성 (JSESSIONID 발급)
            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attrs.getRequest();
            request.getSession(true);

            // 4. 응답
            response.setSuccess(true);
            return response;

        } catch (Exception e) {
            // 아이디 없거나 비번 틀리면 여기로 옴
            response.setSuccess(false);
            return response;
        }
    }
    
    
    
    /*
    public LoginResponse login(LoginRequest dto) {
        LoginResponse response = new LoginResponse();

        // find User
        Optional<SiteUser> siteUserOpt = userRepository.findByUsername(dto.getUsername());

        if (siteUserOpt.isEmpty()) {
            response.setSuccess(false);
            // response.setMessage("[SERVER]: 존재하지 않는 아이디입니다.");
            return response;
        }

        SiteUser user = siteUserOpt.get();

        // Check Password
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            response.setSuccess(false);
            // response.setMessage("[SERVER]: 비밀번호가 일치하지 않습니다.");
            return response;
        }

        response.setSuccess(true);
        // response.setMessage("[SERVER]: 로그인 성공");
        // TODO: 나중에 토큰, 만료시간 등도 여기에

        return response;
    }
	*/
    
    
    @Transactional
    public void modifyUserInfo(String username, UserInfoModifyRequest dto) {

        SiteUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        // Site User
        if (dto.getNickname() != null && !dto.getNickname().isBlank()) {
            user.setNickname(dto.getNickname());
        }
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getBirthday() != null) {
            user.setBirthday(dto.getBirthday());
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            user.setEmail(dto.getEmail());
        }

        // Site User Body
        SiteUserBody body = user.getBodyInfo();
        
        if (dto.getSex() != null) {
            body.setGender(dto.getSex());
        }

        if (dto.getHeight() != null) {
            body.setHeight(dto.getHeight());
        }

        if (dto.getWeight() != null) {
            body.setWeight(dto.getWeight());
        }
        
        if (dto.getUser_grip() != null) {
            body.setUser_grip(dto.getUser_grip());
        }
        
        if (dto.getUser_flex() != null) {
            body.setUser_flex(dto.getUser_flex());
        }
    	
        if (dto.getUser_situp() != null) {
            body.setUser_situp(dto.getUser_situp());
        }
    	
        if (dto.getUser_jump() != null) {
            body.setUser_jump(dto.getUser_jump());
        }
    	
        // userRepository.save(user);

        // return UserInfoResponse.from(user);
    }
    
    
    
    // User Information Response
    public UserInfoResponse getMyInfo(String username) {
    	
    	SiteUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("[SERVER]: 유저가 존재하지 않습니다."));
        
        return new UserInfoResponse(
                user.getId(),
                user.getNickname(),
                user.getUsername(),
                user.getEmail(),
                user.getBirthday(),
                user.getRole().name()
        );
    }
    
    // User Body Information Request
    public void updateBodyInfo(String username, UserBodyInfoRequest dto) {
        SiteUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("[SERVER]: 유저가 없습니다."));

        // Not Exist BodyInfo -> Generation
        SiteUserBody body = userBodyRepository.findByUser(user)
                .orElseGet(() -> {
                    SiteUserBody newBody = SiteUserBody.builder()
                    		.user(user)
                    		.build();
                    
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
                .orElseThrow(() -> new RuntimeException("[SERVER] 유저가 존재하지 않습니다."));
        
        return new UserBodyInfoResponse(
                user.getHeight(),
                user.getWeight(),
                user.getGender().name(),
                user.getUser_grip(),
            	user.getUser_flex(),
            	user.getUser_situp(),
            	user.getUser_jump()
        );
    }
    
    // Profile Photo Update: only URL
    @Transactional
    public void updateProfileImage(Long userId, String imageUrl) {
        SiteUser user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("[SERVER] 유저 없음"));

        user.setProfileImageUrl(imageUrl);
    }
    
    
	// Back-end Test only.
    // Dummy Function
    /*
	public SiteUser create(String username, String email, String password) {
		SiteUser user = SiteUser.builder()
				.username(username)
				.email(email)
				.password(passwordEncoder.encode(password))
				.build();
				
		this.userRepository.save(user);
		return user;
	}

	public SiteUser getUser(String username) {
		Optional<SiteUser> siteUser = this.userRepository.findByUsername(username);
		if (siteUser.isPresent()) {
			return siteUser.get();
		} else {
			throw new DataNotFoundException("[SERVER]: 유저가 존재하지 않습니다.");
		}
	}
	*/
}

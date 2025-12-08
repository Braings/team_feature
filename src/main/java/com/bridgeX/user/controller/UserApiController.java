package com.bridgeX.user.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeX.user.dto.LoginRequest;
import com.bridgeX.user.dto.LoginResponse;
import com.bridgeX.user.dto.SignupRequest;
import com.bridgeX.user.dto.UserBodyInfoRequest;
import com.bridgeX.user.dto.UserBodyInfoResponse;
import com.bridgeX.user.dto.UserInfoModifyRequest;
import com.bridgeX.user.dto.UserInfoResponse;
import com.bridgeX.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

//This is API communication controller

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    
    // Sign-up
    @PostMapping("/sign")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest dto) {
        userService.signup(dto);
        return ResponseEntity.ok("[SERVER] signup success");
    }
    
    // Login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest dto,
            HttpServletRequest request) {

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());

        Authentication authentication = authenticationManager.authenticate(authToken);

        // 1) 비어있는 SecurityContext 생성
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        // 2) SecurityContextHolder 에도 세팅
        SecurityContextHolder.setContext(context);

        // 3) 세션에 SecurityContext 직접 저장
        HttpSession session = request.getSession(true);  // 세션 없으면 생성
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        // 4) 응답
        return ResponseEntity.ok(new LoginResponse(true, null));
    }
    
    
    // User Info Modify
    @PostMapping("/me/modify")
    public ResponseEntity<UserInfoResponse> modifyInfo(Principal principal, @Valid @RequestBody UserInfoModifyRequest request) {
    	// User 정보 꺼내오기
        String username = principal.getName();
        // 변경 요청
        userService.modifyUserInfo(username, request);

        return ResponseEntity.noContent().build();
    }
    
    // User Info Response
    @GetMapping("/users/profile") // 원래 /api/me 였다가 프론트 요청 맞춰서 수정
    public ResponseEntity<UserInfoResponse> getMyInfo(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = principal.getName();
        UserInfoResponse myInfo = userService.getMyInfo(username);
        return ResponseEntity.ok(myInfo);
    }
    
    
    // User BodyInfo Request (edit)
    @PutMapping("/me/body")
    public ResponseEntity<?> putMyBodyInfo(Principal principal, @RequestBody @Valid UserBodyInfoRequest dto){
    	// 로그인 안 됐을 때
    	if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = principal.getName();
        userService.updateBodyInfo(username, dto);
        
        return ResponseEntity.ok().build();
    }
    
    // User BodyInfo Response
    @GetMapping("/me/body")
    public ResponseEntity<UserBodyInfoResponse> getMyBodyInfo(Principal principal){
    	// 로그인 안 됐을 때
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        String username = principal.getName();
        UserBodyInfoResponse UserBodyInfo = userService.getBodyInfo(username);

        return ResponseEntity.ok(UserBodyInfo);        
    }

}


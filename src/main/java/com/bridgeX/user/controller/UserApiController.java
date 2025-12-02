package com.bridgeX.user.controller;

import java.io.IOException;
import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bridgeX.image.ImageService;
import com.bridgeX.user.dto.LoginRequest;
import com.bridgeX.user.dto.LoginResponse;
import com.bridgeX.user.dto.SignupRequest;
import com.bridgeX.user.dto.UserBodyInfoRequest;
import com.bridgeX.user.dto.UserBodyInfoResponse;
import com.bridgeX.user.dto.UserInfoModifyRequest;
import com.bridgeX.user.dto.UserInfoResponse;
import com.bridgeX.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

//This is API communication controller

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final ImageService imageService;
    
    // Sign-up
    @PostMapping("/sign")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest dto) {
        userService.signup(dto);
        return ResponseEntity.ok("signup success");
    }
    
    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest dto) {
        LoginResponse result = userService.login(dto);  // Check login success/fail
        if(!result.isSuccess()) {
        	return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
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
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getMyInfo(Principal principal) {
    	// 로그인 안 됐을 때
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = principal.getName();
        UserInfoResponse MyInfo = userService.getMyInfo(username);

        return ResponseEntity.ok(MyInfo);
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
    
    // User Profile Photo
    @PostMapping("/users/{id}/profile-image")
    public ResponseEntity<?> uploadProfile(@PathVariable Long id,
                                           @RequestPart("image") MultipartFile image) throws IOException {

        String imageUrl = imageService.uploadProfileImage(image, id);
        userService.updateProfileImage(id, imageUrl);

        return ResponseEntity.ok(imageUrl);
    }

}


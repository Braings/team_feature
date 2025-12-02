package com.bridgeX.image;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bridgeX.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProfilePhotoController {

    private final ImageService profileImageService;
    private final UserService userService;

    @PostMapping("/users/{id}/profile-image")
    public ResponseEntity<?> uploadProfile(@PathVariable Long id,
                                           @RequestPart("image") MultipartFile image) throws IOException {

        String imageUrl = profileImageService.uploadProfileImage(image, id);

        userService.updateProfileImage(id, imageUrl);

        return ResponseEntity.ok(imageUrl);
    }
}

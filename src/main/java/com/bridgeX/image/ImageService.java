package com.bridgeX.image;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

/*
	Image Upload Only !!!
 */

@Service
@RequiredArgsConstructor
public class ImageService {

    private final S3Client r2Client;

    @Value("${cloudflare.r2.bucket}")
    private String bucket;

    public String uploadProfileImage(MultipartFile file, Long userId) throws IOException {

        String ext = getExtension(file.getOriginalFilename());
        String key = "profile/" + userId + "/" + UUID.randomUUID() + ext;

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .build();

        r2Client.putObject(request,
                RequestBody.fromBytes(file.getBytes())
        );

        // 설정해둔 PUBLIC 도메인 사용
        return "https://cdn.example.com/" + key;
    }

    private String getExtension(String filename) {
        int idx = filename.lastIndexOf(".");
        return filename.substring(idx);
    }
}

package com.bridgeX;

import java.net.URI;

import software.amazon.awssdk.regions.Region;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.beans.factory.annotation.Value;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class R2Config {

    @Value("${cloudflare.r2.access-key}")
    private String accessKey;

    @Value("${cloudflare.r2.secret-key}")
    private String secretKey;

    @Value("${cloudflare.r2.account-id}")
    private String accountId;

    @Bean
    public S3Client r2Client() {
        String endpoint = String.format("https://%s.r2.cloudflarestorage.com", accountId);

        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .endpointOverride(URI.create(endpoint))
                .region(Region.of("auto")) // R2는 Region 필요 없음
                .build();
    }
}

package com.storytime.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AnonymousCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

@Configuration
public class S3Config {

    private static final Logger log = LoggerFactory.getLogger(S3Config.class);

    @Value("${vultr.access-key}")
    private String accessKey;

    @Value("${vultr.secret-key}")
    private String secretKey;

    @Value("${vultr.bucket-name}")
    private String bucketName;

    @Value("${vultr.endpoint}")
    private String endpoint;

    @Bean
    public S3Client s3Client() {
        AwsCredentialsProvider credentialsProvider;
        if (accessKey == null || accessKey.isBlank() || secretKey == null || secretKey.isBlank()) {
            log.warn("Vultr S3 credentials are not configured; S3 upload features will be unavailable.");
            credentialsProvider = AnonymousCredentialsProvider.create();
        } else {
            AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
            credentialsProvider = StaticCredentialsProvider.create(credentials);
        }

        return S3Client.builder()
                .credentialsProvider(credentialsProvider)
                .region(Region.US_EAST_1)  // 修改为你的区域
                .endpointOverride(URI.create(endpoint))
                .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
                .build();
    }


    public String getBucketName() {
        return bucketName;
    }
}

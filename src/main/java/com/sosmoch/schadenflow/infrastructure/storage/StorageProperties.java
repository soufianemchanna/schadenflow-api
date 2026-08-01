package com.sosmoch.schadenflow.infrastructure.storage;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
@Getter @Setter
public class StorageProperties {
    String bucketName;
    String region;
    String endpoint;
    String accessKey;
    String secretKey;
    boolean pathStyleAccess;
    int presignedUrlExpirationMinutes;
}

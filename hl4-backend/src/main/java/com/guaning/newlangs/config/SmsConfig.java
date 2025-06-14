package com.guaning.newlangs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "sms")
public class SmsConfig {
    private String username;
    private String apiKey;
}
package org.example.postservice.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotEnvConfig {

    private Dotenv dotenv = Dotenv.configure()
            .load();

    @PostConstruct
    public void init() {
        System.setProperty("FIREBASE_CREDENTIALS", dotenv.get("FIREBASE_CREDENTIALS"));
        System.setProperty("S3_ACCESS_KEY", dotenv.get("S3_ACCESS_KEY"));
        System.setProperty("S3_SECRET_KEY", dotenv.get("S3_SECRET_KEY"));
        System.setProperty("USER_SERVICE_ID", dotenv.get("USER_SERVICE_ID"));
    }
}
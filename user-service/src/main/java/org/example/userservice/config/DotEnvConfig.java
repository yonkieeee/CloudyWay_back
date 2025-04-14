package org.example.userservice.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotEnvConfig {

    private Dotenv dotenv = Dotenv.configure().load();

    @PostConstruct
    public void setProperties() {
        System.setProperty("FIREBASE_CREDENTIALS", dotenv.get("FIREBASE_CREDENTIALS"));
        System.setProperty("FIREBASE_URL", dotenv.get("FIREBASE_URL"));
        System.setProperty("MAIL_PASSWORD", dotenv.get("MAIL_PASSWORD"));
        System.setProperty("REDIS_HOST", dotenv.get("REDIS_HOST"));
        System.setProperty("REDIS_PASSWORD", dotenv.get("REDIS_PASSWORD"));
        System.setProperty("S3_ACCESS_KEY", dotenv.get("S3_ACCESS_KEY"));
        System.setProperty("S3_SECRET_KEY", dotenv.get("S3_SECRET_KEY"));
    }
}

package org.example.hereapiservice.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotEnvConfig {

    private final Dotenv dotenv = Dotenv.configure().load();

    @PostConstruct
    public void init() {
        System.setProperty("HERE_API_KEY", dotenv.get("HERE_API_KEY"));
        System.setProperty("RABBITMQ_URL", dotenv.get("RABBITMQ_URL"));
        System.setProperty("AWS_ACCESS_KEY", dotenv.get("AWS_ACCESS_KEY"));
        System.setProperty("AWS_SECRET_KEY", dotenv.get("AWS_SECRET_KEY"));
    }
}


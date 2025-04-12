package org.example.hereapiservice.beans;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotEnvBean {

    @Bean
    public Dotenv dotenv(){
        return Dotenv.configure().load();
    }
}

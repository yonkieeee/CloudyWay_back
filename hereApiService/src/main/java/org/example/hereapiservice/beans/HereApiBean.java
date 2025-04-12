package org.example.hereapiservice.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HereApiBean {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}


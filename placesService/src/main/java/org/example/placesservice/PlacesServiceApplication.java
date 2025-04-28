package org.example.placesservice;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PlacesServiceApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().load();
        System.setProperty("DATABASE_URL", dotenv.get("DATABASE_URL"));
        System.setProperty("DATABASE_USERNAME", dotenv.get("DATABASE_USERNAME"));
        System.setProperty("DATABASE_PASSWORD", dotenv.get("DATABASE_PASSWORD"));
        System.setProperty("OPEN_SEARCH_HOST", dotenv.get("OPEN_SEARCH_HOST"));
        System.setProperty("OPEN_SEARCH_PORT", dotenv.get("OPEN_SEARCH_PORT"));
        System.setProperty("OPEN_SEARCH_USERNAME", dotenv.get("OPEN_SEARCH_USERNAME"));
        System.setProperty("OPEN_SEARCH_PASSWORD", dotenv.get("OPEN_SEARCH_PASSWORD"));
        SpringApplication.run(PlacesServiceApplication.class, args);
    }

}

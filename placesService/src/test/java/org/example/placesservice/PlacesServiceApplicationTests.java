package org.example.placesservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:postgresql://localhost:5432/test_db_placeholder",
        "spring.datasource.username=test_user_placeholder",
        "spring.datasource.password=test_password_placeholder",
        "spring.liquibase.enabled=false"
})
class PlacesServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}

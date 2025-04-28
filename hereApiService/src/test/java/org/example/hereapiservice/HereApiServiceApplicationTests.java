package org.example.hereapiservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest
@TestPropertySource(properties = {
        "aws.region=eu-central-1",
        "aws.accessKey=AWS_TEST_ACCESS_KEY",
        "aws.secretKey=AWS_TEST_SECRET_KEY"
})
class HereApiServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}


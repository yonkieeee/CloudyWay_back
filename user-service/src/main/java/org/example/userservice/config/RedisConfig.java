package org.example.userservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.UnifiedJedis;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.password}")
    private String redisPassword;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public UnifiedJedis unifiedJedis() {
        JedisClientConfig config = DefaultJedisClientConfig.builder()
                .user("default")
                .password(redisPassword)
                .build();

        return new UnifiedJedis(
                new HostAndPort(
                        redisHost,
                        redisPort),
                config
        );
    }
}

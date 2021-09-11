package com.example.redissonspring.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
@ConfigurationProperties(prefix = "redis")
public class RedisConfig {
    private String address;
    private int pingConnectionInterval;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();

        config
                .useSingleServer()
                .setAddress(address)
                .setPingConnectionInterval(pingConnectionInterval);

        return Redisson.create(config);
    }

//    @Bean
//    public RedisTemplate<String, Object> redisTemplate() {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(new RedissonConnectionFactory(
//                redissonClient()
//        ));
//
//        redisTemplate.setHashKeySerializer(RedisSerializer.string());
//        redisTemplate.setKeySerializer(RedisSerializer.string());
//
//        redisTemplate.setEnableTransactionSupport(true);
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPingConnectionInterval() {
        return pingConnectionInterval;
    }

    public void setPingConnectionInterval(int pingConnectionInterval) {
        this.pingConnectionInterval = pingConnectionInterval;
    }
}

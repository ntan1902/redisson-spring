package com.example.redissonspring;

import com.example.redissonspring.config.RedisConfig;
import com.example.redissonspring.entity.User;
import com.example.redissonspring.util.GsonUtils;
import com.example.redissonspring.util.JacksonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.codec.KryoCodec;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;


@SpringBootApplication
public class RedissonSpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(RedissonSpringApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner run(RedisTemplate<String, Object> redisTemplate) {
//        return args -> {
//            redisTemplate.boundHashOps("users:1").put("1", new User(1L, "An1", 21));
//            User user1 = (User) redisTemplate.boundHashOps("users:1").get("1");
//            System.out.println(user1);
//        };
//    }

//    @Bean
//    public CommandLineRunner run(StringRedisTemplate redisTemplate) {
//        return args -> {
//            final String PREFIX_KEY = "users";
//
//            String userId1 = "1";
//            String userId2 = "2";
//
//            // users
//            ObjectMapper om = new ObjectMapper();
//            redisTemplate.boundHashOps(PREFIX_KEY + ":" + userId1).put(userId1, JacksonUtils.toJson(new User(1L, "An1", 21)));
//            redisTemplate.boundHashOps(PREFIX_KEY + ":" + userId2).put(userId2, GsonUtils.toJson(new User(2L, "An2", 21)));
//
//            // get user in key users:1 with deserialize using JacksonJson
//            User user1 = JacksonUtils.fromJson((String) redisTemplate.boundHashOps(PREFIX_KEY + ":" + userId1).get(userId1), User.class);
//            System.out.println(PREFIX_KEY + "-> " + user1);
//
//            // get user in key users:2 with deserialize using Gson
//            User user2 = GsonUtils.fromJson((String) redisTemplate.boundHashOps(PREFIX_KEY + ":" + userId2).get(userId2), User.class);
//            System.out.println(PREFIX_KEY + "-> " + user2);
//        };
//    }

    @Bean
    public CommandLineRunner run(RedissonClient redissonClient) {
        return args -> {
            final String PREFIX_KEY = "users";

            String userId1 = "1";
            String userId2 = "2";
            String userId3 = "3";
            String userId4 = "4";

            // Hash
            redissonClient
                    .getMap(PREFIX_KEY + ":" + userId1)
                    .fastPut(userId1,
                            GsonUtils.toJson(
                                    new User(1L, "An1", 21)
                            )
                    );
            User user1 = GsonUtils.fromJson(
                    (String) redissonClient
                            .getMap(PREFIX_KEY + ":" + userId1)
                            .get(userId1),
                    User.class
            );
            System.out.println(PREFIX_KEY + "-> " + user1);

            // Hash eviction after 10 seconds
            redissonClient
                    .getMapCache(PREFIX_KEY + ":" + userId2)
                    .fastPut(userId2,
                            GsonUtils.toJson(
                                    new User(2L, "An2", 21)
                            ),
                            10,
                            TimeUnit.SECONDS
                    );
            User user2 = GsonUtils.fromJson(
                    (String) redissonClient
                            .getMapCache(PREFIX_KEY + ":" + userId2)
                            .get(userId2),
                    User.class
            );
            System.out.println(PREFIX_KEY + "-> " + user2);

            // Value string
            redissonClient
                    .getBucket(PREFIX_KEY + ":" + userId3)
                    .set(
                            GsonUtils.toJson(
                                    new User(3L, "An3", 21)
                            )
                    );
            User user3 = GsonUtils.fromJson(
                    (String) redissonClient
                            .getBucket(PREFIX_KEY + ":" + userId3)
                            .get(),
                    User.class
            );
            System.out.println(PREFIX_KEY + "-> " + user3);

            // Value string eviction after 10 seconds
            redissonClient
                    .getBucket(PREFIX_KEY + ":" + userId4)
                    .set(
                            GsonUtils.toJson(
                                    new User(4L, "An3", 21)
                            ),
                            10,
                            TimeUnit.SECONDS
                    );
            User user4 = GsonUtils.fromJson(
                    (String) redissonClient
                            .getBucket(PREFIX_KEY + ":" + userId4)
                            .get(),
                    User.class
            );
            System.out.println(PREFIX_KEY + "-> " + user4);
        };
    }
}

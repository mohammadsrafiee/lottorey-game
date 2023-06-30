package com.lottery.game.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

@SpringBootTest
public class UserModelServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private final int userCount = 5;

    @Test
    void testSave() {
        for (int index = 0; index < 5; ++index) {
            UserModel user = new UserModel();
            user.setId(UUID.randomUUID());
            userService.save(user);
            Assertions.assertEquals(user, userService.get(user.getId()));
        }
    }

    @Test
    void testLoad() {
        redisTemplate.execute(
                (RedisCallback<Object>) connection -> {
                    connection.flushAll();
                    return null;
                });
        Set<UserModel> users = new HashSet<>();
        for (int index = 0; index < userCount; ++index) {
            UserModel user = new UserModel();
            user.setId(UUID.randomUUID());
            userService.save(user);
            users.add(user);
        }

        HashSet result = new HashSet(userService.load());
        Assertions.assertEquals(users, result);
    }


}
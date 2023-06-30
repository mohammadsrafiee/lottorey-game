package com.lottery.game.redis;

import com.lottery.game.user.UserModel;
import com.lottery.game.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class RedisCommandTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserService userService;

    @Test
    public void deleteAllUsers() {
        redisTemplate.execute(
                (RedisCallback<Object>) connection -> {
                    connection.flushAll();
                    return null;
                });
    }

    @Test
    public void registerUser() {
        for (int index = 0; index < 1; ++index) {
            UserModel userModel = new UserModel();
            userModel.setId(UUID.randomUUID());
            userService.save(userModel);
        }
    }
}

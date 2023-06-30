package com.lottery.game.lottery;

import com.lottery.game.user.UserModel;
import com.lottery.game.user.UserService;
import com.lottery.game.user.prize.UserPrizeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.UUID;

@SpringBootTest
class LotteryProcessTest {

    @Autowired
    private LotteryProcess process;
    @Autowired
    private UserService userService;
    @Autowired
    private UserPrizeService userPrizeService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void tetProcess() {
        redisTemplate.execute(
                (RedisCallback<Object>) connection -> {
                    connection.flushAll();
                    return null;
                });
        userService.save(new UserModel(UUID.randomUUID()));
        List<UserModel> users = userService.load();
        users.forEach(user -> {
            for (int index = 0; index < 10; ++index) {
                process.lottery(user.getId());
            }
        });
        users.forEach(user -> {
            Assertions.assertNotEquals(null, userPrizeService.get(user.getId()));
        });
    }

}
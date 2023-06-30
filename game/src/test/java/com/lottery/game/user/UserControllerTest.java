package com.lottery.game.user;

import com.lottery.game.lottery.LotteryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private LotteryService lotteryService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private List<UserModel> users = new ArrayList<>();
    private final int userCount = 5;

    @BeforeEach
    public void clear() {
        redisTemplate.execute(
                (RedisCallback<Object>) connection -> {
                    connection.flushAll();
                    return null;
                });
        for (int index = 0; index < userCount; ++index) {
            UserModel user = new UserModel();
            user.setId(UUID.randomUUID());
            userService.save(user);
        }
        users = userService.load();
    }

    @Test
    public void testLoadUsers() throws Exception {
        mockMvc.perform(get(UserController.endpoint))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(userCount)));
    }

    @Test
    public void testGetPrizes() {
        users.forEach(user -> {
            for (int index = 0; index < 10; ++index) {
                lotteryService.startLottery(user.getId());
            }
        });
        users.forEach(user -> {
            try {
                mockMvc.perform(get(UserController.endpoint + "/{userId}/prizes", user.getId().toString()))
                        .andExpect(status().isOk());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

}
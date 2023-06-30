package com.lottery.game.lottery;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lottery.game.user.UserModel;
import com.lottery.game.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class LotteryControllerAsyncTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private List<UserModel> users = new ArrayList<>();

    @BeforeEach
    public void clear() {
        redisTemplate.execute(
                (RedisCallback<Object>) connection -> {
                    connection.flushAll();
                    return null;
                });

        for (int index = 0; index < 10; ++index) {
            UserModel user = new UserModel();
            user.setId(UUID.randomUUID());
            userService.save(user);
        }

        users = userService.load();
    }

    @Test
    public void testLottery() {
        users
                .stream()
                .parallel()
                .forEach(user -> {
                    for (int action = 0; action <= 4; ++action) {
                        try {
                            LotteryRequest request = new LotteryRequest();
                            request.setId(user.getId().toString());
                            mockMvc.perform(MockMvcRequestBuilders.post(LotterySyncController.endpoint)
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(request)))
                                    .andExpect(MockMvcResultMatchers.status().is(202));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    @Test
    public void testInvalidInput() {
        for (int index = 0; index < 5; ++index) {
            for (int action = 0; action <= 4; ++action) {
                try {
                    LotteryRequest request = new LotteryRequest();
                    request.setId("1");
                    mockMvc.perform(MockMvcRequestBuilders.post(LotterySyncController.endpoint)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request)))
                            .andExpect(MockMvcResultMatchers.status().is(500));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Test
    public void testExtraTryPerDayInput() {
        users
                .stream()
                .parallel()
                .forEach(user -> {
                    for (int action = 0; action <= 4; ++action) {
                        try {
                            LotteryRequest request = new LotteryRequest();
                            request.setId(user.getId().toString());
                            mockMvc.perform(MockMvcRequestBuilders.post(LotterySyncController.endpoint)
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(request)))
                                    .andExpect(MockMvcResultMatchers.status().is(202));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }
}

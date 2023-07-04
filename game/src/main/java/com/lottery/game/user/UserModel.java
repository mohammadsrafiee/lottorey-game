package com.lottery.game.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("User")
public class UserModel {
    private UUID id;
    private LocalDate today;
    private int participate;
    private String prizes;
}

package com.lottery.game.user.prize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("UserPrize")
public class UserPrizeModel {
    private UUID id; // USER-ID
    private LocalDate today;
    private int participate;
    private String prizes;
}
package com.lottery.game.lottery;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class LotteryService {
    private final LotteryProcess process;

    public LotteryService(LotteryProcess process) {
        this.process = process;
    }

    @Async
    public CompletableFuture<Void> startLottery(UUID userId) {
        return CompletableFuture.runAsync(() -> process.lottery(userId));
    }
}
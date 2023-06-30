package com.lottery.game.lottery;

import com.lottery.game.core.exception.LotteryException;
import com.lottery.game.core.exception.LotteryExceptionType;

import java.util.UUID;

public class LotteryController {
    protected final LotteryService lotteryService;

    public LotteryController(LotteryService lotteryService) {
        this.lotteryService = lotteryService;
    }

    protected void validation(LotteryRequest request) {
        try {
            if (request != null &&
                    request.getId() != null &&
                    !request.getId().isBlank()) {
                UUID.fromString(request.getId());
                return;
            }
        } catch (Exception ex) {
            throw new LotteryException(LotteryExceptionType.INPUT_DATA_IS_NOT_VALID_EXCEPTION);
        }
        throw new LotteryException(LotteryExceptionType.INPUT_DATA_IS_NOT_VALID_EXCEPTION);
    }
}
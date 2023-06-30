package com.lottery.game.core.exception;

public class ExceptionTranslator {

    public static LotteryExceptionType translate(Throwable ex) {
        if (ex instanceof LotteryException) {
            LotteryException lotteryException = (LotteryException) ex;
            if (lotteryException.getExceptionType() != null) {
                return lotteryException.getExceptionType();
            } else {
                return LotteryExceptionType.UNKNOWN_EXCEPTION;
            }
        } else {
            return LotteryExceptionType.UNKNOWN_EXCEPTION;
        }
    }
}

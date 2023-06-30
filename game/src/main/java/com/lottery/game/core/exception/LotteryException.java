package com.lottery.game.core.exception;

public class LotteryException extends RuntimeException {

    private LotteryExceptionType exceptionType;

    public LotteryException() {
        super();
    }

    public LotteryException(Throwable throwable) {
        super(throwable);
    }

    public LotteryException(String message) {
        super(message);
    }

    public LotteryException(LotteryExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public LotteryException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public LotteryException(LotteryExceptionType exceptionType, Throwable throwable) {
        super(throwable);
        this.exceptionType = exceptionType;
    }

    public LotteryExceptionType getExceptionType() {
        return exceptionType;
    }
}

package com.lottery.game.core.exception;

public enum LotteryExceptionType {

    INCREMENT_EXCEPTION("Happen an exception in process execution.", 500),
    DECREMENT_EXCEPTION("Happen an exception in process execution.", 500),
    LOTTERY_EXECUTION_EXCEPTION("Happen an exception in process execution.", 500),
    USER_NOT_EXIST_EXCEPTION("UserId is not valid", 400),
    INPUT_DATA_IS_NOT_VALID_EXCEPTION("input data is not valid", 500),
    UNKNOWN_EXCEPTION("Unknown exception", 500),
    ;

    LotteryExceptionType(String message, int code) {
        this.message = message;
        this.code = code;
    }

    private final String message;
    private final int code;

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}

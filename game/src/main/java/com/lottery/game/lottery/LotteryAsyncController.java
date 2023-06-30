package com.lottery.game.lottery;

import com.lottery.game.core.exception.ExceptionTranslator;
import com.lottery.game.core.exception.LotteryExceptionType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.UUID;

@RestController
@RequestMapping(LotteryAsyncController.endpoint)
public class LotteryAsyncController extends LotteryController {

    public static final String endpoint = "/api/v2/lottery";

    public LotteryAsyncController(LotteryService lotteryService) {
        super(lotteryService);
    }

    @PostMapping
    public DeferredResult<ResponseEntity<String>> startLottery(@RequestBody LotteryRequest request) {
        DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>();
        try {
            validation(request);
            lotteryService
                    .startLottery(UUID.fromString(request.getId()))
                    .whenComplete((result, exception) -> {
                        if (exception != null) {
                            LotteryExceptionType ex = ExceptionTranslator.translate(exception);
                            deferredResult.setErrorResult(ResponseEntity.status(ex.getCode()).body(ex.getMessage()));
                        } else {
                            deferredResult.setResult(ResponseEntity.ok(null));
                        }
                    });
            return deferredResult;
        } catch (Exception exception) {
            LotteryExceptionType ex = ExceptionTranslator.translate(exception);
            deferredResult.setErrorResult(ResponseEntity.status(ex.getCode()).body(ex.getMessage()));
        }
        return deferredResult;
    }
}

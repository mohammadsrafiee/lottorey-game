package com.lottery.game.lottery;

import com.lottery.game.core.exception.ExceptionTranslator;
import com.lottery.game.core.exception.LotteryExceptionType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(LotterySyncController.endpoint)
public class LotterySyncController extends LotteryController {

    public static final String endpoint = "/api/v1/lottery";

    public LotterySyncController(LotteryService lotteryService) {
        super(lotteryService);
    }

    @PostMapping
    public ResponseEntity<String> startLottery(@RequestBody LotteryRequest request) {
        try {
            validation(request);
            lotteryService.startLottery(UUID.fromString(request.getId()));
            return ResponseEntity.accepted().body("Lottery process started");
        } catch (Exception ex) {
            LotteryExceptionType exception = ExceptionTranslator.translate(ex);
            return ResponseEntity.status(exception.getCode()).body(exception.getMessage());
        }
    }
}
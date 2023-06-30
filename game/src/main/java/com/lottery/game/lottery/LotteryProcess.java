package com.lottery.game.lottery;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.lottery.game.core.DateUtil;
import com.lottery.game.core.exception.LotteryException;
import com.lottery.game.core.exception.LotteryExceptionType;
import com.lottery.game.user.UserModel;
import com.lottery.game.user.UserService;
import com.lottery.game.user.prize.UserPrizeModel;
import com.lottery.game.user.prize.UserPrizeService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class LotteryProcess {
    private static final LinkedHashMap<String, Double> prizes;
    private final UserService userService;
    private final UserPrizeService userPrizeService;

    static {
        prizes = new LinkedHashMap<>();
        prizes.put("A", 0.1);
        prizes.put("B", 0.3);
        prizes.put("C", 0.2);
        prizes.put("D", 0.15);
        prizes.put("E", 0.25);
    }

    public LotteryProcess(UserService userService, UserPrizeService userPrizeService) {
        this.userService = userService;
        this.userPrizeService = userPrizeService;
    }

    public void lottery(UUID userId) {
        UserPrizeModel userPrize = null;
        try {
            userPrize = userPrizeService.get(userId);
            UserModel user = userService.get(userId);
            LocalDate today = DateUtil.today();
            if (user != null) {
                int participationCount = incrementParticipationCount(userPrize, userId);
                if (participationCount <= 3) {
                    String prize = givePrize();
                    if (userPrize == null) {
                        userPrize = new UserPrizeModel();
                    }
                    userPrize.setId(userId);
                    userPrize.setToday(today);
                    userPrize.setPrizes(createPrize(userPrize, today.toString(), prize));
                    userPrizeService.save(userPrize);
                } else {
                    decrementParticipationCount(userPrize, userId);
                }
            } else {
                throw new LotteryException(LotteryExceptionType.USER_NOT_EXIST_EXCEPTION);
            }
        } catch (LotteryException ex) {
            decrementParticipationCount(userPrize, userId);
            throw ex;
        } catch (Exception ex) {
            decrementParticipationCount(userPrize, userId);
            throw new LotteryException(LotteryExceptionType.LOTTERY_EXECUTION_EXCEPTION, ex);
        }
    }

    private synchronized int incrementParticipationCount(UserPrizeModel userPrize, UUID userId) {
        try {
            if (userPrize == null) {
                userPrize = new UserPrizeModel();
                userPrize.setId(userId);
            }
            LocalDate today = DateUtil.today();
            int currentParticipation = 0;
            LocalDate current = userPrize.getToday();
            if ((current != null) && today.isEqual(current)) {
                currentParticipation = userPrize.getParticipate();
            }
            int participation = currentParticipation + 1;
            userPrize.setParticipate(participation);
            userPrize.setToday(today);
            userPrizeService.save(userPrize);
            return participation;
        } catch (Exception ex) {
            throw new LotteryException(LotteryExceptionType.INCREMENT_EXCEPTION, ex.getCause());
        }
    }

    private synchronized void decrementParticipationCount(UserPrizeModel userPrize, UUID userId) {
        try {
            if (userPrize != null) {
                int participationCount = (userPrize.getParticipate() - 1);
                userPrize.setParticipate(Math.max(participationCount, 0));
                userPrizeService.save(userPrize);
            }
        } catch (Exception ex) {
            throw new LotteryException(LotteryExceptionType.DECREMENT_EXCEPTION, ex.getCause());
        }
    }

    private String givePrize() {
        try {
            Thread.sleep(5000L);
            double totalWeight = prizes.values().stream().mapToDouble(Double::doubleValue).sum();
            double randomValue = new Random().nextDouble() * totalWeight;
            double weightSum = 0;
            for (Map.Entry<String, Double> item : prizes.entrySet()) {
                weightSum += item.getValue();
                if (randomValue < weightSum) return item.getKey();
            }
        } catch (Exception ex) {
            throw new LotteryException(LotteryExceptionType.UNKNOWN_EXCEPTION, ex.getCause());
        }
        return null;
    }

    private String createPrize(UserPrizeModel userPrize, String today, String prize) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String str = userPrize.getPrizes();
            Map<String, LinkedList<String>> histories;
            if (str != null && !str.isBlank()) {
                TypeFactory typeFactory = objectMapper.getTypeFactory();
                MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, LinkedList.class);
                histories = objectMapper.readValue(str, mapType);
            } else {
                histories = new HashMap<>();
            }
            LinkedList<String> prizes = histories.get(today);
            if (prizes == null) prizes = new LinkedList<>();
            prizes.add(prize);
            histories.put(today, prizes);
            return objectMapper.writeValueAsString(histories);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new LotteryException(LotteryExceptionType.UNKNOWN_EXCEPTION, ex.getCause());
        }
    }
}

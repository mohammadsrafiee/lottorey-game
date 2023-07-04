package com.lottery.game.lottery;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.lottery.game.core.DateUtil;
import com.lottery.game.core.exception.LotteryException;
import com.lottery.game.core.exception.LotteryExceptionType;
import com.lottery.game.user.UserModel;
import com.lottery.game.user.UserService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class LotteryProcess {
    private static final LinkedHashMap<String, Double> prizes;
    private final UserService userService;

    static {
        prizes = new LinkedHashMap<>();
        prizes.put("A", 0.1);
        prizes.put("B", 0.3);
        prizes.put("C", 0.2);
        prizes.put("D", 0.15);
        prizes.put("E", 0.25);
    }

    public LotteryProcess(UserService userService) {
        this.userService = userService;
    }

    public void lottery(UUID userId) {
        UserModel user = null;
        try {
            user = userService.get(userId);
            if (user != null) {
                int participation = incrementParticipationCount(user);
                if (participation != -1) {
                    user.setPrizes(createPrize(user, givePrize()));
                    userService.save(user);
                }
            } else {
                throw new LotteryException(LotteryExceptionType.USER_NOT_EXIST_EXCEPTION);
            }
        } catch (Exception ex) {
            decrementParticipationCount(user);
            if (!(ex instanceof LotteryException)) {
                throw new LotteryException(LotteryExceptionType.LOTTERY_EXECUTION_EXCEPTION, ex);
            } else {
                throw ex;
            }
        }
    }

    private synchronized int incrementParticipationCount(UserModel user) {
        int result = -1;
        try {
            LocalDate today = DateUtil.today();
            int currentParticipation = 0;
            LocalDate current = user.getToday();
            if ((current != null) && today.isEqual(current)) {
                currentParticipation = user.getParticipate();
            }
            if (currentParticipation < 3) {
                result = currentParticipation + 1;
                user.setParticipate(result);
                user.setToday(today);
                userService.save(user);
            }
            return result;
        } catch (Exception ex) {
            throw new LotteryException(LotteryExceptionType.INCREMENT_EXCEPTION, ex.getCause());
        }
    }

    private synchronized void decrementParticipationCount(UserModel user) {
        try {
            if (user != null) {
                int participation = user.getParticipate();
                if (participation > 0 && participation <= 3) {
                    user.setParticipate((participation - 1));
                    userService.save(user);
                }
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

    private String createPrize(UserModel user, String prize) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = user.getPrizes();
            Map<String, LinkedList<String>> histories;
            if (json != null && !json.isBlank()) {
                TypeFactory typeFactory = objectMapper.getTypeFactory();
                MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, LinkedList.class);
                histories = objectMapper.readValue(json, mapType);
            } else {
                histories = new HashMap<>();
            }
            String today = user.getToday().toString();
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

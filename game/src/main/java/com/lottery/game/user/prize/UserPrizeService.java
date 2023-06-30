package com.lottery.game.user.prize;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserPrizeService {
    private final UserPrizeRepository repository;

    public UserPrizeService(UserPrizeRepository repository) {
        this.repository = repository;
    }

    public void save(UserPrizeModel userPrize) {
        repository.save(userPrize);
    }

    public UserPrizeModel get(UUID UUID) {
        return repository
                .findById(UUID)
                .orElse(null);
    }
}

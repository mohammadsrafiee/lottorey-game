package com.lottery.game.user;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public void save(UserModel userModel) {
        repository.save(userModel);
    }

    public UserModel get(UUID UUID) {
        return repository
                .findById(UUID)
                .orElse(null);
    }

    public List<UserModel> load() {
        return IterableUtils.toList(repository.findAll());
    }
}

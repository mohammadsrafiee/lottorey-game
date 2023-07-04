package com.lottery.game.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(UserController.endpoint)
public class UserController {
    public static final String endpoint = "/api/v1/user";

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/{userId}/prizes")
    public UserModel getUser(@PathVariable("userId") String userId) {
        return userService.get(UUID.fromString(userId));
    }

    @GetMapping
    public List<UserModel> getUsers() {
        return userService.load();
    }

}

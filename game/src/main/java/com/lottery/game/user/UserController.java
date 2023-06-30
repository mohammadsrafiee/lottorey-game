package com.lottery.game.user;

import com.lottery.game.user.prize.UserPrizeModel;
import com.lottery.game.user.prize.UserPrizeService;
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

    private final UserPrizeService userPrizeService;
    private final UserService userService;

    public UserController(UserPrizeService userPrizeService,
                          UserService userService) {
        this.userPrizeService = userPrizeService;
        this.userService = userService;
    }

    @GetMapping(value = "/{userId}/prizes")
    public UserPrizeModel getUserPrizes(@PathVariable("userId") String userId) {
        return userPrizeService.get(UUID.fromString(userId));
    }

    @GetMapping
    public List<UserModel> getUsers() {
        return userService.load();
    }

}

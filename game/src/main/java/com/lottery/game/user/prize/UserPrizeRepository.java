package com.lottery.game.user.prize;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserPrizeRepository extends CrudRepository<UserPrizeModel, UUID> {

}

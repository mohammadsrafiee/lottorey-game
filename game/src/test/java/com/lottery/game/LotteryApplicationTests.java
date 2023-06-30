package com.lottery.game;

import com.lottery.game.lottery.LotterySyncController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@WebMvcTest(LotterySyncController.class)
class LotteryApplicationTests {

	@Test
	void contextLoads() {
	}

}

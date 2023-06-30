package com.lottery.game.user.prize;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserPrizeServiceTest {
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private UserService userService;
//
//    @Test
//    public void testStartLottery() {
//
//        List<UserModel> users = new ArrayList<>();
//        for (int index = 0; index < 10; ++index) {
//            UserModel user = new UserModel();
//            user.setId(UUID.randomUUID());
//            userService.save(user);
//            users.add(user);
//        }
//
//        users.forEach(user -> {
//            for (int action = 0; action <= 4; ++action) {
//                try {
//                    LotteryRequest request = new LotteryRequest();
//                    request.setId(user.getId().toString());
//                    mockMvc.perform(MockMvcRequestBuilders.post(LotteryController.endpoint)
//                                    .contentType(MediaType.APPLICATION_JSON)
//                                    .content(objectMapper.writeValueAsString(request)))
//                            .andExpect(MockMvcResultMatchers.status().is(202));
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
//    }
}
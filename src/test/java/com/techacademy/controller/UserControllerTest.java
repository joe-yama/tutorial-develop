package com.techacademy.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.asm.TypeReference;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techacademy.entity.User;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class UserControllerTest {
  private MockMvc mockMvc;

  private final WebApplicationContext webApplicationContext;

  UserControllerTest(WebApplicationContext context) {
    this.webApplicationContext = context;
  }

  @BeforeEach
  void beforeEach() {
    // Spring Securityを有効にする
    mockMvc =
        MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
  }

  @Test
  @DisplayName("User更新画面")
  @WithMockUser
  void testGetUser() throws Exception {
    // HTTPリクエストに対するレスポンスの検証
    MvcResult result = mockMvc.perform(get("/user/update/1/")) // URLにアクセス
        .andExpect(status().isOk()) // ステータスを確認
        .andExpect(model().attributeExists("user")) // Modelの内容を確認
        .andExpect(model().hasNoErrors()) // Modelのエラー有無の確認
        .andExpect(view().name("user/update")) // viewの確認
        .andReturn(); // 内容の取得

    // userの検証
    // Modelからuserを取り出す
    User user = (User) result.getModelAndView().getModel().get("user");
    assertEquals(1, user.getId());
    assertEquals("キラメキ太郎", user.getName());
  }

  @Test
  @DisplayName("User一覧画面")
  @WithMockUser
  void testGetList() throws Exception {
    Map<Integer, String> expected = new HashMap<>();
    expected.put(1, "キラメキ太郎");
    expected.put(2, "キラメキ次郎");
    expected.put(3, "キラメキ花子");

    MvcResult result = mockMvc.perform(get("/user/list")) // URLにアクセス
        .andExpect(status().isOk()) // ステータスを確認
        .andExpect(model().attributeExists("userlist")) // Modelの内容を確認
        .andExpect(model().hasNoErrors()) // Modelのエラー有無の確認
        .andExpect(view().name("user/list")) // viewの確認
        .andReturn(); // 内容の取得
    ArrayList<?> resultList = (ArrayList<?>) result.getModelAndView().getModel().get("userlist");
    ArrayList<User> users = new ArrayList<User>();
    for (Object object : resultList) {
      if (object instanceof User) {
        users.add((User) object);
      }
    }
    assertEquals(3, users.size());
    for (User user : users) {
      assertEquals(expected.get(user.getId()), user.getName());
    }
  }
}

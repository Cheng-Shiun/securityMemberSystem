package com.chengshiun.securityMemberManagerSystem;

import com.chengshiun.securityMemberManagerSystem.dto.MemberRegisterRequest;
import com.chengshiun.securityMemberManagerSystem.dto.MemberUpdateRequest;
import com.chengshiun.securityMemberManagerSystem.model.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class securityMemberManagementSystemApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    //認證
    //未認證能存取的公開 api -> 會員註冊
    //未使用 csrf 保護
    @Test
    public void testMemberRegister_permitAll_success() throws Exception {
        //模擬一個會員註冊請求
        MemberRegisterRequest memberRegisterRequest = new MemberRegisterRequest();
        memberRegisterRequest.setEmail("test@gmail.com");
        memberRegisterRequest.setPassword("test");
        memberRegisterRequest.setName("Test");
        memberRegisterRequest.setAge(20);

        String json = objectMapper.writeValueAsString(memberRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/member/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201));
    }

    //有認證帳號存取 /hello api
    @Test
    public void testHello_success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/hello")
                .with(httpBasic("test1@gmail.com", "111"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200));
    }

    //授權
    //限制只有 ADMIN 才能存取 /getMember api
    @Test
    @WithMockUser(username = "mock", roles = {"NORMAL_MEMBER"})
    public void testGetMember_notAdminRole() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/getMember/{memberId}", 2);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(403));
    }


    @Test
    public void testGetMember_AdminRole_success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/member/getMember/{memberId}", 2)
                .with(httpBasic("test1@gmail.com", "111"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200));
    }

    //授權
    //有受 CSRF 保護的 api
    @Test
    @Transactional
    public void testApiWithCsrf_AnyRole_success() throws Exception {
        //模擬使用者要更新的會員數據
        MemberUpdateRequest memberUpdateRequest = new MemberUpdateRequest();
        memberUpdateRequest.setPassword("newPassword");
        memberUpdateRequest.setName("newName");
        memberUpdateRequest.setAge(30);

        String json = objectMapper.writeValueAsString(memberUpdateRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/member/update/{memberId}", 1)
                .with(httpBasic("test3@gmail.com", "333"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200));
    }

    @Test
    @Transactional
    public void testApiWithoutCsrf_AnyRole() throws Exception {
        //模擬使用者要更新的會員數據
        MemberUpdateRequest memberUpdateRequest = new MemberUpdateRequest();
        memberUpdateRequest.setPassword("newPassword");
        memberUpdateRequest.setName("newName");
        memberUpdateRequest.setAge(30);

        String json = objectMapper.writeValueAsString(memberUpdateRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/member/update/{memberId}", 1)
                .with(httpBasic("test3@gmail.com", "333"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(403));
    }

    @Test
    @Transactional
    public void testDeleteMember_success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/member/delete/{memberId}", 3)
                .with(httpBasic("test1@gmail.com", "111"))
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(204));
    }

    //CORS 檢查 Preflight 請求
    @Test
    public void testCors() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .options("/hello")
                .header("Access-Control-Request-Method", "GET")
                .header("Access-Control-Request-Headers", "Authorization")
                .header("Origin", "http://www.example.com");

        mockMvc.perform(requestBuilder)
                .andExpect(header().exists("Access-Control-Allow-Origin"))
                .andExpect(header().string("Access-Control-Allow-Origin", "*"))
                .andExpect(header().exists("Access-Control-Allow-Methods"))
                .andExpect(header().string("Access-Control-Allow-Methods", "GET"))
                .andExpect(header().exists("Access-Control-Allow-Headers"))
                .andExpect(header().string("Access-Control-Allow-Headers", "Authorization"))
                .andExpect(header().exists("Access-Control-Max-Age"))
                .andExpect(header().string("Access-Control-Max-Age", "3600"))
                .andExpect(status().is(200));
    }

    //授權
    //Post、Put、Delete 角色 api -> 僅 ADMIN 能存取
    @Test
    @Transactional
    public void testAddRoleToMember_success() throws Exception {
        //模擬註冊一個新會員
        MemberRegisterRequest memberRegisterRequest = new MemberRegisterRequest();
        memberRegisterRequest.setEmail("test@gmail.com");
        memberRegisterRequest.setPassword("test");
        memberRegisterRequest.setName("Test");
        memberRegisterRequest.setAge(20);

        String json = objectMapper.writeValueAsString(memberRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/member/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201));

        Integer role_id = 2;  //設定要新增的 role_id

        //模擬新增角色給新會員
        RequestBuilder requestBuilder2 = MockMvcRequestBuilders
                .post("/member/{memberId}/roles", 4)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(role_id))       // 將 Integer 轉換為 String 並設置到 content 中
                .with(httpBasic("test1@gmail.com", "111"))
                .with(csrf());

        mockMvc.perform(requestBuilder2)
                .andExpect(status().is(201));
    }

    @Test
    @Transactional
    public void testUpdateRole_success() throws Exception {
        Integer role_id = 3;  //設定要修改的 role_id

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/member/{memberId}/roles", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(role_id))       // 將 Integer 轉換為 String 並設置到 content 中
                .with(httpBasic("test1@gmail.com", "111"))
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200));
    }

    @Test
    @Transactional
    public void testDeleteRole_success() throws Exception {
        Integer role_id = 3;  //設定要刪除的 role_id
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/member/{memberId}/roles", 3)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(role_id))       // 將 Integer 轉換為 String 並設置到 content 中
                .with(httpBasic("test1@gmail.com", "111"))
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200));
    }


}

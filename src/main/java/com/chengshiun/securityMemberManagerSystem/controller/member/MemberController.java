package com.chengshiun.securityMemberManagerSystem.controller.member;

import com.chengshiun.securityMemberManagerSystem.dto.MemberRegisterRequest;
import com.chengshiun.securityMemberManagerSystem.dto.MemberResetPasswordRequest;
import com.chengshiun.securityMemberManagerSystem.dto.MemberUpdateRequest;
import com.chengshiun.securityMemberManagerSystem.model.Member;
import com.chengshiun.securityMemberManagerSystem.model.Role;
import com.chengshiun.securityMemberManagerSystem.service.MemberService;
import com.chengshiun.securityMemberManagerSystem.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private RoleService roleService;

    //註冊會員帳號
    @PostMapping("/member/register")
    public ResponseEntity<Member> register(@RequestBody @Valid MemberRegisterRequest memberRegisterRequest) {
        Integer memberId = memberService.register(memberRegisterRequest);

        //預設給註冊成功的 member 添加 normal_member 的 role
        Role normalMemberRole = roleService.getRoleByName("ROLE_NORMAL_MEMBER");
        roleService.addRoleForMemberId(memberId, normalMemberRole);

        Member member = memberService.getMemberById(memberId);

        return ResponseEntity.status(HttpStatus.CREATED).body(member);
    }

    //查詢會員帳號
    @GetMapping("/member/getMember/{memberId}")
    public ResponseEntity<Member> getMember(@PathVariable Integer memberId) {
        Member member = memberService.getMemberById(memberId);

        if (member == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(member);
        }
    }

    //修改會員資料(密碼、姓名、年齡) (修改資料的權限角色為 所有角色)
    //(修改 member_has_role table 數據的權限角色需為 ROLE_ADMIN)
    @PutMapping("/member/update")
    public ResponseEntity<?> updateMember(Authentication authentication,
                                          @RequestBody MemberUpdateRequest memberUpdateRequest) {
        //取得使用者的帳號
        String username = authentication.getName();

        Member member = memberService.updateMember(username, memberUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).body(member);
    }

    //刪除會員資料 (刪除資料的權限角色為 ADMIN)
    @DeleteMapping("/member/delete/{memberId}")
    public ResponseEntity<?> deleteMember(@PathVariable Integer memberId) {
        memberService.deleteMemberById(memberId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //會員忘記密碼
    @PostMapping("/member/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        try {
            //返回提示訊息
            String responseMessage = String.format("密碼重置信已寄出至 %s\n", email);

            //暫用 response body 取代 member 的收信匣
            //會員取得 reset-password 驗證連結
            String url = "https://example.com/reset-password?token=" + memberService.forgotPassword(email);
            responseMessage = responseMessage + "重置密碼的驗證信連結: " + url;

            responseMessage = responseMessage + "\n請於24小時內更改密碼";

            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    //會員收到驗證認證信 重置密碼
    @PutMapping("/member/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email,
                                           @RequestParam String token,
                                           @RequestBody MemberResetPasswordRequest memberResetPasswordRequest) {
        //驗證 Token k的生成時間
        try {
            memberService.resetPassword(email, token, memberResetPasswordRequest);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Password reset successful");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

}

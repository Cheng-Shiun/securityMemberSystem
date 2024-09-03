package com.chengshiun.securityMemberManagerSystem.controller.member;

import com.chengshiun.securityMemberManagerSystem.dto.MemberRegisterRequest;
import com.chengshiun.securityMemberManagerSystem.dto.MemberUpdateRequest;
import com.chengshiun.securityMemberManagerSystem.model.Member;
import com.chengshiun.securityMemberManagerSystem.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MemberController {

    @Autowired
    private MemberService memberService;

    //註冊會員帳號
    @PostMapping("/member/register")
    public ResponseEntity<Member> register(@RequestBody @Valid MemberRegisterRequest memberRegisterRequest) {
        Integer memberId = memberService.register(memberRegisterRequest);

        Member member = memberService.getMemberById(memberId);

        return ResponseEntity.status(HttpStatus.CREATED).body(member);
    }

    //查詢會員帳號
    @GetMapping("/member/getMember/{memberId}")
    public ResponseEntity<Member> getMember(@PathVariable Integer memberId) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getMemberById(memberId));
    }

    //修改會員資料(密碼、姓名、年齡) (修改角色資料需為 ROLE_ADMIN 才能使用)
    @PutMapping("/member/update/{memberId}")
    public ResponseEntity<?> updateMember(@PathVariable Integer memberId,
                                          @RequestBody MemberUpdateRequest memberUpdateRequest) {

        Member member = memberService.updateMember(memberId, memberUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).body(member);
    }
}

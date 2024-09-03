package com.chengshiun.securityMemberManagerSystem.controller.member;

import com.chengshiun.securityMemberManagerSystem.dto.MemberRegisterRequest;
import com.chengshiun.securityMemberManagerSystem.model.Member;
import com.chengshiun.securityMemberManagerSystem.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class memberController {

    @Autowired
    private MemberService memberService;

    //註冊會員帳號
    @PostMapping("/member/register")
    public ResponseEntity<Member> register(@RequestBody @Valid MemberRegisterRequest memberRegisterRequest) {
        Integer memberId = memberService.register(memberRegisterRequest);

        Member member = memberService.getMemberById(memberId);

        return ResponseEntity.status(HttpStatus.CREATED).body(member);
    }
}

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
        Member member = memberService.getMemberById(memberId);

        if (member == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(member);
        }
    }

    //修改會員資料(密碼、姓名、年齡) (修改資料的權限角色為 所有角色)
    //(修改 member_has_role table 數據的權限角色需為 ROLE_ADMIN)
    @PutMapping("/member/update/{memberId}")
    public ResponseEntity<?> updateMember(@PathVariable Integer memberId,
                                          @RequestBody MemberUpdateRequest memberUpdateRequest) {
        Member member = memberService.updateMember(memberId, memberUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).body(member);
    }

    //刪除會員資料 (刪除資料的權限角色為 所有角色)
    @DeleteMapping("/member/delete/{memberId}")
    public ResponseEntity<?> deleteMember(@PathVariable Integer memberId) {
        memberService.deleteMemberById(memberId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

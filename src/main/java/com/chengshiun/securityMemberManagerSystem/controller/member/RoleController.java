package com.chengshiun.securityMemberManagerSystem.controller.member;

import com.chengshiun.securityMemberManagerSystem.model.Member;
import com.chengshiun.securityMemberManagerSystem.service.MemberService;
import com.chengshiun.securityMemberManagerSystem.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private MemberService memberService;

    //新增角色給指定會員 -> 僅 ADMIN 可使用此 api
    @PostMapping("/member/{memberId}/roles")
    public ResponseEntity<Member> addRoleToMember(@PathVariable Integer memberId,
                                             @RequestBody Integer roleId) {
        roleService.addRoleToMember(memberId, roleId);

        Member member = memberService.getMemberById(memberId);

        return ResponseEntity.status(HttpStatus.CREATED).body(member);
    }

    //修改會員角色 -> 僅 ADMIN 可使用此 api
    @PutMapping("/member/{memberId}/roles")
    public ResponseEntity<Member> updateRole(@PathVariable Integer memberId,
                                             @RequestBody Integer roleId) {
        roleService.updateRole(memberId, roleId);

        Member member = memberService.getMemberById(memberId);

        return ResponseEntity.status(HttpStatus.OK).body(member);
    }

    //刪除會員角色 -> 僅 ADMIN 可使用此 api
    @DeleteMapping("/member/{memberId}/roles")
    public ResponseEntity<?> deleteRole(@PathVariable Integer memberId,
                                        @RequestBody Integer roleId) {
        roleService.deleteRole(memberId, roleId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

package com.chengshiun.securityMemberManagerSystem.dao;

import com.chengshiun.securityMemberManagerSystem.dto.MemberRegisterRequest;
import com.chengshiun.securityMemberManagerSystem.dto.MemberResetPasswordRequest;
import com.chengshiun.securityMemberManagerSystem.dto.MemberUpdateRequest;
import com.chengshiun.securityMemberManagerSystem.model.Member;
import com.chengshiun.securityMemberManagerSystem.model.Role;

import java.util.List;

public interface MemberDao {

    Member getMemberById(Integer memberId);

    Member getMemberByEmail(String email);

    Integer createMember(MemberRegisterRequest memberRegisterRequest);

    Member updateMember(String username, MemberUpdateRequest memberUpdateRequest);

    List<Role> getRolesByMemberId(Integer memberId);

    void deleteMemberById(Integer memberId);

    void resetPassword(String username, MemberResetPasswordRequest memberResetPasswordRequest);
}

package com.chengshiun.securityMemberManagerSystem.service;

import com.chengshiun.securityMemberManagerSystem.dto.MemberRegisterRequest;
import com.chengshiun.securityMemberManagerSystem.dto.MemberResetPasswordRequest;
import com.chengshiun.securityMemberManagerSystem.dto.MemberUpdateRequest;
import com.chengshiun.securityMemberManagerSystem.model.Member;

public interface MemberService {

    Member getMemberById(Integer memberId);

    Integer register(MemberRegisterRequest memberRegisterRequest);

    Member updateMember(String username, MemberUpdateRequest memberUpdateRequest);

    void deleteMemberById(Integer memberId);

    String forgotPassword(String email);

    String resetPassword(String email, String token, MemberResetPasswordRequest memberResetPasswordRequest);
}

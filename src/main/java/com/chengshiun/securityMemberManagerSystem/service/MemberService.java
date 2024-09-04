package com.chengshiun.securityMemberManagerSystem.service;

import com.chengshiun.securityMemberManagerSystem.dto.MemberRegisterRequest;
import com.chengshiun.securityMemberManagerSystem.dto.MemberUpdateRequest;
import com.chengshiun.securityMemberManagerSystem.model.Member;

public interface MemberService {

    Member getMemberById(Integer memberId);

    Integer register(MemberRegisterRequest memberRegisterRequest);

    Member updateMember(Integer memberId, MemberUpdateRequest memberUpdateRequest);

    void deleteMemberById(Integer memberId);
}

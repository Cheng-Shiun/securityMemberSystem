package com.chengshiun.securityMemberManagerSystem.dao;

import com.chengshiun.securityMemberManagerSystem.dto.MemberRegisterRequest;
import com.chengshiun.securityMemberManagerSystem.model.Member;

public interface MemberDao {

    Member getMemberById(Integer memberId);
    Member getMemberByEmail(String email);

    Integer createMember(MemberRegisterRequest memberRegisterRequest);
}

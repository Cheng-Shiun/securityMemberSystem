package com.chengshiun.securityMemberManagerSystem.service;

import com.chengshiun.securityMemberManagerSystem.model.Member;

public interface RoleService {

    Member addRoleToMember(Integer memberId, Integer roleId);

    Member updateRole(Integer memberId, Integer roleId);

    void deleteRole(Integer memberId, Integer roleId);

}

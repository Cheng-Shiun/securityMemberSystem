package com.chengshiun.securityMemberManagerSystem.service;

import com.chengshiun.securityMemberManagerSystem.model.Member;
import com.chengshiun.securityMemberManagerSystem.model.Role;

public interface RoleService {

    Member addRoleToMember(Integer memberId, Integer roleId);

    Member updateRole(Integer memberId, Integer roleId);

    void deleteRole(Integer memberId, Integer roleId);

    Role getRoleByName(String roleName);

    void addRoleForMemberId(Integer memberId, Role role);
}

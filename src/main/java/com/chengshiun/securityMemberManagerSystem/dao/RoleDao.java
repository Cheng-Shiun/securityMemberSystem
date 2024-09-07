package com.chengshiun.securityMemberManagerSystem.dao;

import com.chengshiun.securityMemberManagerSystem.model.Role;

import java.util.List;

public interface RoleDao {
    void addRoleToMember(Integer memberId, Integer roleId);

    List<Integer> getRoleIdsByMemberId(Integer memberId);

    void updateRole(Integer memberId, Integer roleId);

    void deleteRole(Integer memberId, Integer roleId);

    Role getRoleByName(String roleName);

    void addRoleForMemberId(Integer memberId, Role role);
}

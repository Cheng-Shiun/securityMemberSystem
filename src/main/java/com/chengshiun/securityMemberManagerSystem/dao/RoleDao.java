package com.chengshiun.securityMemberManagerSystem.dao;

import java.util.List;

public interface RoleDao {
    void addRoleToMember(Integer memberId, Integer roleId);

    List<Integer> getRoleIdsByMemberId(Integer memberId);

    void updateRole(Integer memberId, Integer roleId);

    void deleteRole(Integer memberId, Integer roleId);
}

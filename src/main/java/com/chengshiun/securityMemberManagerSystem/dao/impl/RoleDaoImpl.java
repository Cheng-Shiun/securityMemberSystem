package com.chengshiun.securityMemberManagerSystem.dao.impl;

import com.chengshiun.securityMemberManagerSystem.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RoleDaoImpl implements RoleDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void addRoleToMember(Integer memberId, Integer roleId) {
        String sql = """
                INSERT INTO member_has_role (member_id, role_id) VALUES (:memberId, :roleId)
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);
        map.put("roleId", roleId);

        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public List<Integer> getRoleIdsByMemberId(Integer memberId) {
        String sql = """
                SELECT role_id FROM member_has_role
                WHERE member_id = :memberId
                """;

        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);

        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> rs.getInt("role_id"));
    }

    @Override
    public void updateRole(Integer memberId, Integer roleId) {
        String sql = """
                UPDATE member_has_role SET role_id = :roleId WHERE member_id = :memberId
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("roleId", roleId);
        map.put("memberId", memberId);

        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public void deleteRole(Integer memberId, Integer roleId) {
        String sql = """
                DELETE FROM member_has_role WHERE member_id = :memberId AND role_id = :roleId
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);
        map.put("roleId" , roleId);

        namedParameterJdbcTemplate.update(sql, map);
    }
}

package com.chengshiun.securityMemberManagerSystem.rowmapper;

import com.chengshiun.securityMemberManagerSystem.constant.MemberRole;
import com.chengshiun.securityMemberManagerSystem.model.Member;
import com.chengshiun.securityMemberManagerSystem.model.Role;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MemberRowMapper implements RowMapper<Member> {
    @Override
    public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
        Member member = new Member();
        member.setMemberId(rs.getInt("member_id"));
        member.setEmail(rs.getString("email"));
        member.setPassword(rs.getString("password"));
        member.setName(rs.getString("name"));
        member.setAge(rs.getInt("age"));

        // 處理 roles
        List<Role> roles = new ArrayList<>();
        do {
            Role role = new Role();
            role.setRoleName(rs.getString("role_name"));
            roles.add(role);
        } while (rs.next() && rs.getInt("member_id") == member.getMemberId());
        member.setRoles(roles);

        return member;
    }
}

package com.chengshiun.securityMemberManagerSystem.rowmapper;

import com.chengshiun.securityMemberManagerSystem.constant.MemberRole;
import com.chengshiun.securityMemberManagerSystem.model.Member;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

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

        //將資料庫中的 角色 String -> enum
        String roleStr = rs.getString("role_name");
        MemberRole role = MemberRole.valueOf(roleStr);
        member.setRole(role);

        return member;
    }
}

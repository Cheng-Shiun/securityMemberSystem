package com.chengshiun.securityMemberManagerSystem.rowmapper;

import com.chengshiun.securityMemberManagerSystem.constant.MemberRole;
import com.chengshiun.securityMemberManagerSystem.model.Member;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
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

        //將資料庫中的 角色 String -> enum
        Set<MemberRole> roles = new HashSet<>();
        do {
            String roleStr = rs.getString("role_name");
            MemberRole role = MemberRole.valueOf(roleStr);
            roles.add(role);
        } while (rs.next()); // 如果每个 member 可能有多个角色

        return member;
    }
}

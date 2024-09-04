package com.chengshiun.securityMemberManagerSystem.rowmapper;

import com.chengshiun.securityMemberManagerSystem.constant.MemberRole;
import com.chengshiun.securityMemberManagerSystem.model.Member;
import com.chengshiun.securityMemberManagerSystem.model.Role;
import com.chengshiun.securityMemberManagerSystem.security.MyMemberDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
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

        // 處理同個 member 有多個 role_name
        List<String> roleNames = new ArrayList<>();

        // 取得所有角色 rs.next() == null 的時候會跳出 loop
        do {
            String roleName = rs.getString("role_name");
            if (roleName != null) {
                roleNames.add(roleName);
            } else
                break;
        } while (rs.next());

        member.setRoleNames(roleNames);

        return member;
    }
}

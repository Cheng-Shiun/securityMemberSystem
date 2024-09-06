package com.chengshiun.securityMemberManagerSystem.dao.impl;

import com.chengshiun.securityMemberManagerSystem.dao.MemberDao;
import com.chengshiun.securityMemberManagerSystem.dto.MemberRegisterRequest;
import com.chengshiun.securityMemberManagerSystem.dto.MemberResetPasswordRequest;
import com.chengshiun.securityMemberManagerSystem.dto.MemberUpdateRequest;
import com.chengshiun.securityMemberManagerSystem.model.Member;
import com.chengshiun.securityMemberManagerSystem.model.Role;
import com.chengshiun.securityMemberManagerSystem.rowmapper.MemberRowMapper;
import com.chengshiun.securityMemberManagerSystem.rowmapper.RoleRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MemberDaoImpl implements MemberDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private MemberRowMapper memberRowMapper;

    @Autowired
    private RoleRowMapper roleRowMapper;

    @Override
    public Member getMemberById(Integer memberId) {
        String sql = """
                SELECT member.member_id, member.email, member.password, member.name, member.age, role.role_name FROM member
                LEFT JOIN member_has_role ON member.member_id = member_has_role.member_id
                LEFT JOIN role ON member_has_role.role_id = role.role_id
                WHERE member.member_id = :memberId
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);

        List<Member> memberList = namedParameterJdbcTemplate.query(sql, map, memberRowMapper);

        if (memberList.size() > 0) {
            return memberList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Member getMemberByEmail(String email) {
        String sql = """
                SELECT member.member_id, member.email, member.password, member.name, member.age, role.role_name FROM member
                JOIN member_has_role ON member.member_id = member_has_role.member_id
                JOIN role ON member_has_role.role_id = role.role_id
                WHERE member.email = :email
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("email", email);

        List<Member> memberList = namedParameterJdbcTemplate.query(sql, map, memberRowMapper);

        if (memberList.size() > 0) {
            return memberList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Integer createMember(MemberRegisterRequest memberRegisterRequest) {
        String sql = """
                INSERT INTO member(email, password, name, age) VALUES (:email, :password, :name, :age)
                """;

        Map<String, Object> map = new HashMap<>();

        map.put("email", memberRegisterRequest.getEmail());
        map.put("password", memberRegisterRequest.getPassword());
        map.put("name", memberRegisterRequest.getName());
        map.put("age", memberRegisterRequest.getAge());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int memberId = keyHolder.getKey().intValue();

        return memberId;
    }

    @Override
    public Member updateMember(String username, MemberUpdateRequest memberUpdateRequest) {
        //使用 StringBuilder 動態擴充 sql 語法
        StringBuilder sql = new StringBuilder("UPDATE member SET ");
        Map<String, Object> map = new HashMap<>();

        //處理請求參數是否有更新值
        if (memberUpdateRequest.getPassword() != null) {
            sql.append("password = :password, ");
            map.put("password", memberUpdateRequest.getPassword());
        }
        if (memberUpdateRequest.getName() != null) {
            sql.append("name = :name, ");
            map.put("name", memberUpdateRequest.getName());
        }
        if (memberUpdateRequest.getAge() != null) {
            sql.append("age = :age, ");
            map.put("age", memberUpdateRequest.getAge());
        }

        //刪除最後兩個逗號和空格
        int length = sql.length();
        if (length > 0 && sql.substring(length - 2).equals(", ")) {
            sql.setLength(length - 2);
        }

        //添加 WHERE 語句 email值為 使用者的username
        sql.append(" WHERE email = :email");
        map.put("email", username);

        namedParameterJdbcTemplate.update(sql.toString(), map);

        //回傳更新後的數據給前端
        return getMemberByEmail(username);
    }

    @Override
    public List<Role> getRolesByMemberId(Integer memberId) {
        String sql = """
                SELECT role.role_id, role.role_name FROM role
                    JOIN member_has_role ON role.role_id = member_has_role.role_id
                    WHERE member_has_role.member_id = :memberId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);

        List<Role> roleList = namedParameterJdbcTemplate.query(sql, map, roleRowMapper);

        return roleList;
    }

    @Override
    public void deleteMemberById(Integer memberId) {
        String sql = """
                DELETE FROM member WHERE member_id = :memberId
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);

        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public void resetPassword(String username, MemberResetPasswordRequest memberResetPasswordRequest) {
        String sql = "UPDATE member SET password = :password WHERE email = :email";

        Map<String, Object> map = new HashMap<>();
        map.put("password", memberResetPasswordRequest.getNewPassword());
        map.put("email", username);

        namedParameterJdbcTemplate.update(sql, map);
    }
}

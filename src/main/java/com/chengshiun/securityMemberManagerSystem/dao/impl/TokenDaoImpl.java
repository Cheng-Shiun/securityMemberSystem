package com.chengshiun.securityMemberManagerSystem.dao.impl;

import com.chengshiun.securityMemberManagerSystem.dao.TokenDao;
import com.chengshiun.securityMemberManagerSystem.model.Token;
import com.chengshiun.securityMemberManagerSystem.rowmapper.TokenRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TokenDaoImpl implements TokenDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveToken(String email, Token token) {
        String sql = "INSERT INTO token (email, value, created_date) VALUES (:email, :value, :createdDate)";

        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("value", token.getValue());
        map.put("createdDate", token.getCreated_date());

        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public LocalDateTime getCreatedDateByValue(String email, String value) {
        String sql = "SELECT created_date FROM token WHERE value = :value AND email = :email";

        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("value", value);

        return namedParameterJdbcTemplate.queryForObject(sql, map, LocalDateTime.class);
    }

    @Override
    public String getValueByEmail(String email) {
        String sql = "SELECT value FROM token WHERE email = :email";

        Map<String, Object> map = new HashMap<>();
        map.put("email", email);

        List<String> results = namedParameterJdbcTemplate.queryForList(sql, map, String.class);

        if (results.isEmpty()) {
            return null; // 或者根據需要拋出自定義異常
        }
        return results.get(0);
    }
}

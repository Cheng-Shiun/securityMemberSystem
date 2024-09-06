package com.chengshiun.securityMemberManagerSystem.rowmapper;

import com.chengshiun.securityMemberManagerSystem.model.Token;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TokenRowMapper implements RowMapper<Token> {
    @Override
    public Token mapRow(ResultSet rs, int rowNum) throws SQLException {
        Token token = new Token();
        token.setToken_id(rs.getInt("token_id"));
        token.setEmail(rs.getString("email"));
        token.setValue(rs.getString("value"));
        token.setCreated_date(rs.getTimestamp("created_date"));
        return null;
    }
}

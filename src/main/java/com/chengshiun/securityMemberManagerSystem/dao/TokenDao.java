package com.chengshiun.securityMemberManagerSystem.dao;

import com.chengshiun.securityMemberManagerSystem.model.Token;

import java.time.LocalDateTime;

public interface TokenDao {
    void saveToken(String email, Token token);

    LocalDateTime getCreatedDateByValue(String email, String value);

    String getValueByEmail(String email);
}

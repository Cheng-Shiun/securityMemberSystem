package com.chengshiun.securityMemberManagerSystem.dto;

import jakarta.validation.constraints.NotBlank;

public class MemberResetPasswordRequest {
    @NotBlank
    private String newPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

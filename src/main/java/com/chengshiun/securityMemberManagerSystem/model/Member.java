package com.chengshiun.securityMemberManagerSystem.model;

import com.chengshiun.securityMemberManagerSystem.constant.MemberRole;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Set;

public class Member {
    private Integer memberId;
    private String email;

    @JsonIgnore
    private String password;

    private String name;
    private Integer age;

    private List<Role> roles;

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer member_id) {
        this.memberId = member_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}


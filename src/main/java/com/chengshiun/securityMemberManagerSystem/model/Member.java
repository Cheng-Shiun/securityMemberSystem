package com.chengshiun.securityMemberManagerSystem.model;

public class Member {
    private Integer memberId;
    private String email;
    private String password;
    private String name;
    private Integer age;

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
}

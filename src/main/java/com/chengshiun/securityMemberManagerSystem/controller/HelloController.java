package com.chengshiun.securityMemberManagerSystem.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@Controller
public class HelloController {

    @RequestMapping("/hello")
    public String hello(Authentication authentication) {

        //取得使用者的帳號
        String username = authentication.getName();

        //取得使用者的權限
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        return "Hello! " + username + "!";
    }
}

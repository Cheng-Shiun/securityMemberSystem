package com.chengshiun.securityMemberManagerSystem.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @PostMapping("/welcome")
    public String welcome() {
        System.out.println("執行 /welcome");
        return "Welcome!";
    }
}

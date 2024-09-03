package com.chengshiun.securityMemberManagerSystem.security;

import com.chengshiun.securityMemberManagerSystem.dao.MemberDao;
import com.chengshiun.securityMemberManagerSystem.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MyMemberDetailServiceImpl implements UserDetailsService {

    @Autowired
    private MemberDao memberDao;

    //loadUserByUsername() 根據使用者輸入的帳號 查詢資料庫中的會員數據
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //從資料庫中查詢 member 帳號
        Member member = memberDao.getMemberByEmail(username);

        //帳號不存在
        if (member == null) {
            throw new UsernameNotFoundException("Member not found for " + username);
        } else {
            String memberEmail = member.getEmail();
            String memberPassword = member.getPassword();

            //權限
            List<GrantedAuthority> authorities = member.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())) // 直接使用 role.name()
                    .collect(Collectors.toList());

            //該方法預設是返回 Spring Security 指定的 User 格式(帳號, 密碼, 權限)
            return new User(memberEmail, memberPassword, authorities);
        }
    }
}

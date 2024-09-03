package com.chengshiun.securityMemberManagerSystem.security;

import com.chengshiun.securityMemberManagerSystem.dao.MemberDao;
import com.chengshiun.securityMemberManagerSystem.model.Member;
import com.chengshiun.securityMemberManagerSystem.model.Role;
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
            //帳號密碼
            String memberEmail = member.getEmail();
            String memberPassword = member.getPassword();

            //權限
            List<Role> roleList = memberDao.getRolesByMemberId(member.getMemberId());

            //使用自定義轉換方法 存取權限
            List<GrantedAuthority> memberAuthorities = convertToAuthorities(roleList);


            //該方法預設是返回 Spring Security 指定的 User 格式(帳號, 密碼, 權限)
            return new User(memberEmail, memberPassword, memberAuthorities);
        }
    }

    //自定義ArrayList 轉換成 Authority 格式
    private List<GrantedAuthority> convertToAuthorities(List<Role> roleList) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roleList) {
            //將查找出來的role_name都轉換成 GrantedAuthority 類
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return authorities;
    }
}

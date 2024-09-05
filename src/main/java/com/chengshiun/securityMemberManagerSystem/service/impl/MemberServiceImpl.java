package com.chengshiun.securityMemberManagerSystem.service.impl;

import com.chengshiun.securityMemberManagerSystem.dao.MemberDao;
import com.chengshiun.securityMemberManagerSystem.dto.MemberRegisterRequest;
import com.chengshiun.securityMemberManagerSystem.dto.MemberUpdateRequest;
import com.chengshiun.securityMemberManagerSystem.model.Member;
import com.chengshiun.securityMemberManagerSystem.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class MemberServiceImpl implements MemberService {

    private final static Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public Member getMemberById(Integer memberId) {
        return memberDao.getMemberById(memberId);
    }

    @Override
    public Integer register(MemberRegisterRequest memberRegisterRequest) {
        //檢查 email 是否已經註冊過
        Member member = memberDao.getMemberByEmail(memberRegisterRequest.getEmail());

        if (member != null) {
            log.warn("該 email {} 已經被註冊", memberRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //使用 BCrypt 演算法 hash password
        String hashedPassword = passwordEncoder.encode(memberRegisterRequest.getPassword());
        memberRegisterRequest.setPassword(hashedPassword);

        Integer memberId = memberDao.createMember(memberRegisterRequest);

        return memberId;
    }

    @Override
    public Member updateMember(String username, MemberUpdateRequest memberUpdateRequest) {


        //判斷前端有要更新密碼
        if (memberUpdateRequest.getPassword() != null) {

            //修改密碼需經過加密，再存入資料庫中
            String hashedPassword = passwordEncoder.encode(memberUpdateRequest.getPassword());
            memberUpdateRequest.setPassword(hashedPassword);
        }


        Member updatedMember = memberDao.updateMember(username, memberUpdateRequest);

        return updatedMember;
    }

    @Override
    public void deleteMemberById(Integer memberId) {
        memberDao.deleteMemberById(memberId);
    }

    @Override
    public String forgotPassword(String email) {
        Member member = memberDao.getMemberByEmail(email);

        //判斷 member 是否存在
        if (member == null) {
            throw new IllegalArgumentException("member 不存在");
        } else {
            String resetToken = generateResetToken();

            //發送重置密碼的驗證信給 member
            String resetLink = String.format("https://example.com/reset-password?token=%s", resetToken);

            //執行發信
            sendPasswordResetEmail(email, resetLink);

            System.out.println("重置密碼的驗證信連結為: " + resetLink);

            return resetToken;
        }
    }

    //生成 Token
    private String generateResetToken() {
        return UUID.randomUUID().toString();
    }

    //自定義 寄信方法
    public void sendPasswordResetEmail(String to, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("密碼更改 Password Reset Request");
        message.setText("Please click the following link to reset your password:\n" + resetLink);

        mailSender.send(message);
    }
}

package com.chengshiun.securityMemberManagerSystem.service.impl;

import com.chengshiun.securityMemberManagerSystem.dao.MemberDao;
import com.chengshiun.securityMemberManagerSystem.dao.TokenDao;
import com.chengshiun.securityMemberManagerSystem.dto.MemberRegisterRequest;
import com.chengshiun.securityMemberManagerSystem.dto.MemberResetPasswordRequest;
import com.chengshiun.securityMemberManagerSystem.dto.MemberUpdateRequest;
import com.chengshiun.securityMemberManagerSystem.model.Member;
import com.chengshiun.securityMemberManagerSystem.model.Token;
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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
public class MemberServiceImpl implements MemberService {

    private final static Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private TokenDao tokenDao;

    private static final long EXPIRATION_TIME_SECONDS = 30;

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
            //返回 Token value 與 created_date 給前端
            Token resetToken = new Token();
            resetToken.setValue(generateResetToken());
            resetToken.setCreated_date(generateCreatedDate());
            tokenDao.saveToken(email, resetToken);

            //執行發信 暫用 response body 取代 收信夾
//            sendPasswordResetEmail(email, resetLink);

            return resetToken.getValue();
        }
    }

    @Override
    public String resetPassword(String email, String token, MemberResetPasswordRequest memberResetPasswordRequest) {
        //檢查 token 是否還在期限內 -> 可使用
        LocalDateTime currentDateTime = LocalDateTime.now();

        LocalDateTime tokenCreatedDate = tokenDao.getCreatedDateByValue(email, token);

        Duration duration = Duration.between(tokenCreatedDate, currentDateTime);
        long secondsBetween = duration.getSeconds();

        if (secondsBetween > 10) {            // 假設只有設定 10 秒的期限
            throw new IllegalArgumentException("Token 已經過期");
        }

        //檢查前端的 token 與 member 的 token
        if (token.equals(tokenDao.getValueByEmail(email))) {
            //將新密碼加密
            String hashedPassword = passwordEncoder.encode(memberResetPasswordRequest.getNewPassword());
            memberResetPasswordRequest.setNewPassword(hashedPassword);

            memberDao.resetPassword(email, memberResetPasswordRequest);
            return "密碼更改成功，請使用新密碼重新登入！";
        }else {
            System.out.println("token:" + token);
            System.out.println("資料庫中的該 member token 值為:" + tokenDao.getValueByEmail(email));
          throw new IllegalArgumentException("驗證 member email 錯誤");
        }
    }

    //自定義生成 Token
    private String generateResetToken() {
        return UUID.randomUUID().toString();
    }

    //自定義生成時間
    private Date generateCreatedDate() {
        Date now = new Date();
        return now;
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

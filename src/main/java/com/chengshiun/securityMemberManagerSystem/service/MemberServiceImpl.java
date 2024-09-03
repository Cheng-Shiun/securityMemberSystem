package com.chengshiun.securityMemberManagerSystem.service;

import com.chengshiun.securityMemberManagerSystem.dao.MemberDao;
import com.chengshiun.securityMemberManagerSystem.dto.MemberRegisterRequest;
import com.chengshiun.securityMemberManagerSystem.dto.MemberUpdateRequest;
import com.chengshiun.securityMemberManagerSystem.model.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MemberServiceImpl implements MemberService{

    private final static Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberDao memberDao;

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
    public Member updateMember(Integer memberId, MemberUpdateRequest memberUpdateRequest) {
        //查詢是否有該筆會員數據
        Member existingMember = memberDao.getMemberById(memberId);
        if (existingMember == null) {
            log.warn("該 member {} 不存在", memberId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //修改密碼則也要經過加密，再存入資料庫中
        String hashedPassword = passwordEncoder.encode(memberUpdateRequest.getPassword());
        memberUpdateRequest.setPassword(hashedPassword);

        Member updatedMember = memberDao.updateMember(memberId, memberUpdateRequest);


        return updatedMember;
    }
}

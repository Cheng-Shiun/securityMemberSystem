package com.chengshiun.securityMemberManagerSystem.service.impl;

import com.chengshiun.securityMemberManagerSystem.dao.MemberDao;
import com.chengshiun.securityMemberManagerSystem.dao.RoleDao;
import com.chengshiun.securityMemberManagerSystem.model.Member;
import com.chengshiun.securityMemberManagerSystem.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final static Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private MemberDao memberDao;


    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public Member addRoleToMember(Integer memberId, Integer roleId) {
        //檢查該 member 是否存在
        Member member = memberDao.getMemberById(memberId);

        if (member == null) {
            log.warn("該 member {} 不存在", memberId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else {
            roleDao.addRoleToMember(memberId, roleId);
            Member updatedMember = memberDao.getMemberById(memberId);

            return updatedMember;
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public Member updateRole(Integer memberId, Integer roleId) {
        // log the authorities of the current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Current authorities: " + authentication.getAuthorities());

        //判斷更新的 roleId 是否已經存在該 member
        List<Integer> currentRoleIds = roleDao.getRoleIdsByMemberId(memberId);

        if (currentRoleIds.contains(roleId)) {
            log.warn("該 member {} 已經有 role_id 為: {} 角色", memberId, roleId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else {
            roleDao.updateRole(memberId, roleId);

            //返回更新角色的 member
            Member member = memberDao.getMemberById(memberId);
            return member;
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteRole(Integer memberId, Integer roleId) {
        roleDao.deleteRole(memberId, roleId);
    }
}

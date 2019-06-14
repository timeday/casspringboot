package com.cas.service.impl;

import com.cas.dao.RoleDao;
import com.cas.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleDao roleDao;

    @Override
    public String findRolesByUserId(String uid) {
        return roleDao.findRolesByUserId(uid);
    }

    @Override
    public Set<String> findAllRoles() {
        return roleDao.findAllRoles();
    }
}

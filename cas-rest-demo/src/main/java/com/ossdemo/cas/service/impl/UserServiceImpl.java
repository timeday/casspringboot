package com.ossdemo.cas.service.impl;

import com.ossdemo.cas.dao.UserMapper;
import com.ossdemo.cas.dao.entity.SysUser;
import com.ossdemo.cas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public SysUser selectUser(String username) {
        return userMapper.selectUser(username);
    }
}

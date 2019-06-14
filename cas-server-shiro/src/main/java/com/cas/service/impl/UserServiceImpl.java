package com.cas.service.impl;

import com.cas.dao.UserDao;
import com.cas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public Map<String, Object> findByUserName(String userName) {
        return userDao.findByUserName(userName);
    }
}

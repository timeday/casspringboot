package com.ossdemo.cas.service;

import com.ossdemo.cas.dao.entity.SysUser;

public interface UserService {

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    SysUser selectUser(String username);
}

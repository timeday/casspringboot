package com.ossdemo.cas.dao;

import com.ossdemo.cas.dao.entity.SysUser;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    SysUser selectUser(@Param("username") String username);
}

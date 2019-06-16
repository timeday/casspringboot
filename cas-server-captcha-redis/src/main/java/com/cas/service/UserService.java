package com.cas.service;

import java.util.Map;


public interface UserService {

    Map<String,Object> findByUserName(String userName);
}

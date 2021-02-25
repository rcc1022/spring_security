package com.cc.security.service.impl;

import com.cc.security.entity.UserInfo;
import com.cc.security.mapper.UserInfoMapper;
import com.cc.security.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: cc
 * @Date: 2021/2/22 15:57
 * @Description:
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public UserInfo getUserInfo(String username){
        return userInfoMapper.getUserInfoByUsername(username);
    }

}
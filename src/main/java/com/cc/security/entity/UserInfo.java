package com.cc.security.entity;

import lombok.Data;

import java.util.Set;

/**
 * @author: cc
 * @Date: 2021/2/22 15:52
 * @Description:
 */
@Data
public class UserInfo {
    private int id;
    private String username;
    private String password;

    /**
     * 用户对应的角色集合
     */
    private Set<Role> roles;
}

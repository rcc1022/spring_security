package com.cc.security.entity;

import lombok.Data;

import java.util.Set;

/**
 * @author: cc
 * @Date: 2021/2/22 16:33
 * @Description:
 */
@Data
public class Role {

    private Integer id;
    private String name;

    /**
     * 角色对应权限集合
     */
    private Set<Permissions> permissions;
}

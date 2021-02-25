package com.cc.security.mapper;

import com.cc.security.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * @author: cc
 * @Date: 2021/2/22 15:56
 * @Description:
 */
@Mapper
@Repository
public interface UserInfoMapper {
    @Select("select * from Sys_User where username = #{username}")
    UserInfo getUserInfoByUsername(String username);

}

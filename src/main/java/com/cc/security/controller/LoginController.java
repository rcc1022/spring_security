package com.cc.security.controller;

import com.alibaba.fastjson.JSON;
import com.cc.security.annotation.Login;
import com.cc.security.annotation.LoginUser;
import com.cc.security.entity.UserInfo;
import com.cc.security.service.UserInfoService;
import com.cc.security.util.JwtUtil;
import com.cc.security.vo.Result;
import com.google.gson.Gson;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @author: cc
 * @Date: 2021/2/22 15:45
 * @Description:
 */
@RestController
public class LoginController {

    @Autowired
    private UserInfoService userInfoService;

    @Login
    @ResponseBody
    @RequestMapping("/getUser")
    public Result getUser(@RequestParam String username){
        return new Result().setData("new Gson().toJson()");
    }


    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    private JwtUtil jwtUtil;

    @ResponseBody
    @RequestMapping("/api/login")
    public Result login(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletResponse response) {
       /* System.out.println("username=="+username);
        System.out.println("password=="+password);*/
        if (username != null && password != null /*username.equals(password)*/) {
            UserInfo userInfo = userInfoService.getUserInfo(username);
            if (ObjectUtils.isEmpty(userInfo)){
                return Result.error(404,"没有该用户");
            }
            //登录成功，产生令牌，发送给客户端
            String token = jwtUtil.generateToken(userInfo.getUsername());
            redisTemplate.opsForValue().set("USER_TOKEN:"+username, JSON.toJSONString(token), 3, TimeUnit.MINUTES);
            System.out.println("action.token"+token);
            //令牌域名（头子跨域)
            response.addHeader("Access-Control-Expose-Headers","token");
            response.addHeader("token", token);
            return new Result().setData(token);
        } else {
            return Result.error(500,"登录失败");

        }
    }

    @PostMapping("/user")
    @RequiresRoles(logical = Logical.OR,value = {"user","admin"})
    @ResponseBody
    public Result user(){
        return new Result(200,"成功访问user接口！");
    };

    @PostMapping("/admin")
    @RequiresRoles(logical = Logical.OR,value = {"admin"})
    @ResponseBody
    public Object admin() {
        return new Result(200,"成功访问admin接口！");
    };
}

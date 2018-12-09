package com.zzx.shiro.controller;

import com.zzx.shiro.entity.UserEntity;
import com.zzx.shiro.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Created by Mr.John on 2018/11/23 17:49.
 **/

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/loginUser")
    public String loginUser(String userName,String password,HttpSession session) {
    return userService.login(userName,password,session);
    }

    @RequestMapping("/logOut")
    public String logOut(HttpSession session) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
//        session.removeAttribute("user");
        return "login";
    }
    @RequestMapping("/register")
    public Object add(String userName,String password){
        int add=userService.add(userName,password);
        if(add==1){
            return "成功";
        }
        return "失败";
    }

}

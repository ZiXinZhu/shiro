package com.zzx.shiro.controller;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import com.zzx.shiro.dao.UserDao;
import com.zzx.shiro.jpush.JPushService;
import com.zzx.shiro.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mr.John on 2018/11/23 17:49.
 **/

@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    JPushService jPushService;
    @Autowired
    UserDao userDao;

    @RequestMapping("/loginUser")
    public String loginUser(String userName, String password, HttpSession session) {
        return userService.login(userName, password, session);
    }

    @RequestMapping("/logOut")
    public String logOut(HttpSession session) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
//        session.removeAttribute("user");
        return "login";
    }

    @RequestMapping("/register")
    public Object add(String userName, String password) {
        int add = userService.add(userName, password);
        if (add == 1) {
            return "成功";
        }
        return "失败";
    }

    @RequiresRoles("医生")
    @RequiresPermissions("1")
    @RequestMapping("/select")
    public Object select() {
        try {
            return userDao.getOne("aaa", "ea124fc9c09b8d8284e927e66b921760");
        }catch (Exception e){
            return "hello.html";
        }
    }

    @RequestMapping("/role")
    public Object role() {
        try {
            return userDao.getOne("aaa", "ea124fc9c09b8d8284e927e66b921760");
        }catch (Exception e){
            return "404.html";
        }
    }

    @RequestMapping("/jpush")
    public boolean setms(String userId) throws ParseException, APIConnectionException, APIRequestException {
        String s="2018-12-12";
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date date=dateFormat.parse(s);
        return jPushService.withdrawSuccess(userId,date);

    }

}

package com.zzx.shiro.service;

import com.zzx.shiro.dao.UserDao;
import com.zzx.shiro.entity.RoleEnumEntity;
import com.zzx.shiro.entity.UserEntity;
import com.zzx.shiro.enums.UserEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

/**
 * Created by Mr.John on 2018/11/22 21:32.
 **/
@Service
@Slf4j
public class UserService {

    @Autowired
    UserDao userDao;

    public String login(String userName,String password,HttpSession session) {
        UsernamePasswordToken usernamePasswordToken=new UsernamePasswordToken(userName,password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(usernamePasswordToken);   //完成登录
            UserEntity user=(UserEntity) subject.getPrincipal();
            session.setAttribute("user", user);
            subject.checkRole(UserEnum.KXJ.getRoleName());
            subject.checkRole(UserEnum.YS.getRoleName());
            log.info("欢迎用户:{}登录！",((UserEntity) subject.getPrincipal()).getUserName());
            return "1";
        } catch(Exception e) {
            log.info("权限不足");
            return "login.html";//返回登录页面
        }
    }

   public Set<String> getRoleByName(String userName){
      return userDao.getRolesByName(userName);
   }

    public UserEntity get(String userName){
        UserEntity userEntity=userDao.get(userName);
        if(userEntity!=null){
            return userEntity;
        }
        return null;
    }

    public int add(String userName ,String password){
        return userDao.add(userName,password);
    }
}

package com.zzx.shiro.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzx.shiro.dao.UserDao;
import com.zzx.shiro.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Set;

/**
 * Created by Mr.John on 2018/11/22 21:32.
 **/
@Service
@Slf4j
public class UserService {

    @Autowired
    UserDao userDao;

    public String login(String userName, String password, HttpSession session) {
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userName, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(usernamePasswordToken);   //完成登录
            UserEntity user = (UserEntity) subject.getPrincipal();
            session.setAttribute("user", user);
            //subject.checkRole(UserEnum.KXJ.getRoleName());
            //subject.checkRole(UserEnum.YS.getRoleName());
            //log.info("欢迎用户:{}登录！",((UserEntity) subject.getPrincipal()).getUserName());
            subject.checkPermission("1");
            log.info("用户：{}拥有：{}权限", ((UserEntity) subject.getPrincipal()).getUserName(), user.getPower());
            return "1";
        } catch (Exception e) {
            log.info("权限不足");
            return "login.html";//返回登录页面
        }
    }

    public Set<String> getRoleByName(String userName) {
        return userDao.getRolesByName(userName);
    }

    public UserEntity get(String userName) {
        UserEntity userEntity = userDao.get(userName);
        if (userEntity != null) {
            return userEntity;
        }
        return null;
    }

    public int add(String userName, String password) {
        UserEntity userEntity = userDao.get(userName);
        if (userEntity != null) {
            return 0;
        }
       /*
       //加盐：方法一
        Random random=new Random();
        Integer salt=random.nextInt(900000000)+100000000;
        Md5Hash md5Hash=new Md5Hash(password+salt);
        log.info("md5Hash:{}",md5Hash);
        return userDao.add(userName, String.valueOf(md5Hash),salt);*/

        //加盐：方法二
        String algorithmName = "MD5";
        Object source = password;
        Random random = new Random();
        String salt = String.valueOf(random.nextInt(900000000) + 100000000);
        int hashIterations = 1024;
        Object result = new SimpleHash(algorithmName, source, salt, hashIterations);

        return userDao.add(userName, String.valueOf(result), salt);
    }

    public static void md5Hash() {
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            md5.digest("".getBytes("utf8"));

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public PageInfo<UserEntity> page(int pn) {
        //TODO 使用的时候还需要添加maven依赖，application.properties中配置
        return PageHelper.startPage(pn, 3).setOrderBy("salt desc").doSelectPageInfo(() -> userDao.getAll());
        /*PageHelper.startPage(pn, 5);
        List<UserEntity> userEntities = userDao.getAll();
        PageInfo<UserEntity> p=new PageInfo<UserEntity>(userEntities);
        log.info("获取内容：{}", p);
        return p;*/
    }
}

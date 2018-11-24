package com.zzx.shiro.service;

import com.zzx.shiro.dao.UserDao;
import com.zzx.shiro.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Mr.John on 2018/11/22 21:32.
 **/
@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public Object find(String userName,String password){
        int log=userDao.log(userName,password);
        if(log==1){
            return "允许登录";
        }
        return "没有找到";
    }

    public UserEntity get(String userName){
        UserEntity userEntity=userDao.get(userName);
        if(userEntity!=null){
            return userEntity;
        }
        return null;
    }
}

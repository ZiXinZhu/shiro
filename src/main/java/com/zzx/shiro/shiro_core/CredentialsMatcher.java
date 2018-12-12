package com.zzx.shiro.shiro_core;

import com.zzx.shiro.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Mr.John on 2018/11/22 22:03.
 **/
@Slf4j
public class CredentialsMatcher extends SimpleCredentialsMatcher {

    @Autowired
    UserService userService;

    /**
     * 密码验证
     * @param token
     * @param info
     * @return
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken utoken=(UsernamePasswordToken) token;
        //获得用户输入的密码:(可以采用加盐(salt)的方式去检验)
        String inPassword = new String(utoken.getPassword());
        //获得数据库中的密码
        String dbPassword=(String) info.getCredentials();
        /*//反加密：方法一
        String salt=String.valueOf((userService.get(utoken.getUsername())).getSalt());
        String md5Hash= String.valueOf(new Md5Hash(inPassword+salt));
        log.info("输入密码：{}，盐:{}，密码加盐:{}",inPassword,salt,md5Hash);*/

        //反加密：方法二
        String algorithmName  ="MD5";
        Object source =inPassword;
        String salt=String.valueOf((userService.get(utoken.getUsername())).getSalt());
        int  hashIterations =1024;
        Object result =  new SimpleHash(algorithmName, source, salt, hashIterations);
        //进行密码的比对
        return this.equals(String.valueOf(result), dbPassword);
    }
}

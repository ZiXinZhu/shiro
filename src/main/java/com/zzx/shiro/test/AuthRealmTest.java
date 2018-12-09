package com.zzx.shiro.test;

import com.zzx.shiro.shiro_core.AuthRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * Created by Mr.John on 2018/12/8 19:10.
 **/
public class AuthRealmTest {
    @Test
    public void Authentication(){
        AuthRealm authRealm=new AuthRealm();

        DefaultSecurityManager defaultSecurityManager=new DefaultSecurityManager();
        defaultSecurityManager.setRealm(authRealm);

        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject=SecurityUtils.getSubject();

        UsernamePasswordToken usernamePasswordToken=new UsernamePasswordToken("aaa","111");
        subject.login(usernamePasswordToken);


    }
}

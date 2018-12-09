package com.zzx.shiro.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * Created by Mr.John on 2018/12/8 16:30.
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShiroTest {
    SimpleAccountRealm simpleAccountRealm=new SimpleAccountRealm();

    @Before
    public void adduser(){
        simpleAccountRealm.addAccount("zzx","123","admin","user");
    }

    @Test
    public void Authentication(){
        DefaultSecurityManager defaultSecurityManager=new DefaultSecurityManager();
        defaultSecurityManager.setRealm(simpleAccountRealm);
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject=SecurityUtils.getSubject();

        UsernamePasswordToken usernamePasswordToken=new UsernamePasswordToken("zzx","123");
        subject.login(usernamePasswordToken);
        subject.checkRoles("admin","user");

    }
}

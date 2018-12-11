package com.zzx.shiro.shiro_core;

import com.zzx.shiro.entity.RoleEntity;
import com.zzx.shiro.entity.RoleEnumEntity;
import com.zzx.shiro.entity.UserEntity;
import com.zzx.shiro.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Mr.John on 2018/11/22 21:43.
 **/
public class AuthRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;



    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken utoken = (UsernamePasswordToken) authenticationToken;
        //从传过来的认证信息中获取用户名
        String userName = utoken.getUsername();
        //通过用户名到数据库中获取凭证，通过用户名获取密码
        UserEntity user = userService.get(userName);
        if (user == null) {
            return null;
        }
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(), this.getClass().getName());
        // return new SimpleAuthenticationInfo(user, user.getPassword(), this.getClass().getName());
        return simpleAuthenticationInfo;
    }
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        UserEntity user = (UserEntity) principalCollection.fromRealm(this.getClass().getName()).iterator().next();//获取session中的用户
        List<String> permissions = new ArrayList<>();
        permissions.add(String.valueOf(user.getPower()));
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(permissions);//将权限放入shiro中.
        //获取并添加用户所有角色
        Set<String> roles = userService.getRoleByName(user.getUserName());
        info.setRoles(roles);

        return info;
    }

}

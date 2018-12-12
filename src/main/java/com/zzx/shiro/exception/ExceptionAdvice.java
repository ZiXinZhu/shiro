package com.zzx.shiro.exception;

import com.zzx.shiro.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.transform.Result;

/**
 * Created by Mr.John on 2018/12/11 21:51.
 **/
@Slf4j
@ControllerAdvice
public class ExceptionAdvice extends Exception{

    @ExceptionHandler({AuthorizationException.class})
    @ResponseBody
    public Object handleException(Exception e){
//        e.printStackTrace();
        log.info("--------------AuthorizationException--------------");
        return "你没得权限，爬！";
    }

}

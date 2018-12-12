package com.zzx.shiro.jpush;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import com.zzx.shiro.dao.JPushDao;
import com.zzx.shiro.exception.CommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.xml.ws.Action;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by Mr.John on 2018/12/12 21:08.
 **/
@Service
public class JPushService implements IJPushServer{
    private static String APPKEY;
    private static String MASTERSECRET;
    JPushClient jPushClient = new JPushClient("f3ed2e4ed451eeae708de0a3", "2f49ffde4d491a854a8ec3b4");
    @Autowired
    JPushDao jPushDao;
    @Override
    public boolean withdrawSuccess(String userId, Date date) throws APIConnectionException, APIRequestException {
        //jpush.setEnableSSL(true);
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
        LocalDateTime localDate = instant.atZone(zoneId).toLocalDateTime();
        String word = String.format("[提现成功]您于%s月%s日%s时%s分提交的提现申请平台已处理成功，钱款将在1-3个工作日到达您的账户,请及时查收",localDate.getMonthValue(),localDate.getDayOfMonth(),localDate.getHour(),localDate.getMinute());
        String[] users = new String[]{userId};
        if(checkPushResult(jPushClient.sendMessageAll(word))){
            return jPushDao.setms(word,userId,String.valueOf(new Date().getTime()))==1;
        }else throw new CommonException("发送失败");
    }

    @Override
    public boolean withdrawFailed(String userId, Date date, String reason) throws APIConnectionException, APIRequestException {
        //jpush.setEnableSSL(true);
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
        LocalDateTime localDate = instant.atZone(zoneId).toLocalDateTime();
        String word = String.format("[提现失败]您于%s月%s日%s时%s分提交的提现申请,平台处理失败，原因如下:%s",localDate.getMonthValue(),localDate.getDayOfMonth(),localDate.getHour(),localDate.getMinute(),reason);
        String[] users = new String[]{userId};
        if(checkPushResult(jPushClient.sendMessageAll(word))){
            return jPushDao.setms(word,userId,String.valueOf(new Date().getTime()))==1;
        }else throw new CommonException("发送失败");
    }

    @Override
    public boolean emptyPositionRemind(String userId, String content) throws APIConnectionException, APIRequestException {
        String[] users = new String[]{userId};
        String word = "[空位提醒]"+content+"场地终于空出了太阳能电池板购买份额啦！赶紧开抢吧";
        if (checkPushResult(jPushClient.sendMessageAll(word))){
            return jPushDao.setms(word,userId,String.valueOf(new Date().getTime()))==1;
        }else throw new CommonException("发送失败");
    }
    private boolean checkPushResult(PushResult pushResult){
        if (pushResult.getResponseCode() == 200)
            return true;
        else return false;
    }
}

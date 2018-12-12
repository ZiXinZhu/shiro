package com.zzx.shiro.jpush;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;

import java.util.Date;

/**
 * Created by Mr.John on 2018/12/12 21:09.
 **/
public interface IJPushServer {
    boolean withdrawSuccess(String userId,Date date) throws APIConnectionException, APIRequestException;

    boolean withdrawFailed(String userId,Date date,String reason) throws APIConnectionException, APIRequestException;

    boolean emptyPositionRemind(String userId,String content) throws APIConnectionException, APIRequestException;

}

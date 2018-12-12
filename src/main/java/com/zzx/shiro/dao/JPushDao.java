package com.zzx.shiro.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Mr.John on 2018/12/12 21:41.
 **/
@Mapper
public interface JPushDao {

    @Insert("INSERT INTO jpush (message,user_id,time)VALUES(#{message},#{userId},#{time})")
    int setms(@Param("message")String message,
              @Param("userId")String userId,
              @Param("time")String time);
}

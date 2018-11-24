package com.zzx.shiro.dao;

import com.zzx.shiro.entity.UserEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by Mr.John on 2018/11/22 20:57.
 **/
@Mapper
public interface UserDao {
    @Insert("INSERT INTO user (user_name,password) VALUES (#{userName},#{password})")
    int add(@Param("userName") String userName,
            @Param("password") String password);

    @Select("SELECT count(id) FROM user WHERE user_name=#{userName} and password=#{password}")
    int log(@Param("userName") String userName,
            @Param("password") String password);

    @Select("SELECT * FROM user WHERE user_name=#{userName}")
    UserEntity get(@Param("userName") String userName);
}

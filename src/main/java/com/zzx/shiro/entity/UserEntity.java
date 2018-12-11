package com.zzx.shiro.entity;

import lombok.Data;
import lombok.ToString;

/**
 * Created by Mr.John on 2018/11/22 20:55.
 **/

@Data
@ToString
public class UserEntity {
    private int id;
    private String userName;
    private String Password;
    private int power;
    private String salt;
}

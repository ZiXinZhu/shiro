package com.zzx.shiro.entity;

import lombok.Data;
import lombok.ToString;

/**
 * Created by Mr.John on 2018/12/4 11:34.
 **/

@Data
@ToString
public class RoleEntity {
    private int id;
    private int userId;
    private int role;
}

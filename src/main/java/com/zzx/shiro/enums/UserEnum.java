package com.zzx.shiro.enums;

/**
 * Created by Mr.John on 2018/12/9 19:20.
 **/
public enum  UserEnum {
    JS(1,"教师"),
    YS(2,"医生"),
    KXJ(3,"科学家");

    private int id;
    private String roleName;

    UserEnum(int id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public int getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }
}

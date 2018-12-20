package com.zzx.shiro.test;

/**
 * Created by Mr.John on 2018/12/15 22:27.
 **/
public interface Formult{
    double calculate(int a);
    default double sqrt(int a) {
        return Math.sqrt(a);
    }
}
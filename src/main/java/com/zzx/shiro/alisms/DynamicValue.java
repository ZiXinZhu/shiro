package com.zzx.shiro.alisms;

import com.aliyuncs.IAcsClient;
import com.zzx.shiro.exception.CommonException;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

public class DynamicValue
{
    private static Key application_user_key;
    private static Key application_admin_key;
    private static IAcsClient acsClient;
    private static Map<String, String> phoneAndCode = new HashMap<>();

    public static String getCode(String phone)
    {
        if (phoneAndCode.containsKey(phone))
        {
            String code = phoneAndCode.get(phone);
            phoneAndCode.remove(phone);
            return code;
        } else
            return null;
    }

    public static void addPhoneAndCode(String phone, String code)
    {
        if (phone == null || code == null || phone.equals("") || code.equals(""))
            throw new CommonException("参数不能为空或空值!");
        removePhone(phone);
        phoneAndCode.put(phone, code);
    }

    public static boolean isContainsPhone(String phone)
    {
        return phoneAndCode.containsKey(phone);
    }

    public static void removePhone(String phone)
    {
        if (phoneAndCode.containsKey(phone))
            phoneAndCode.remove(phone);
    }

    public static Key getApplication_user_key()
    {
        return application_user_key;
    }

    public static Key getApplication_admin_key()
    {
        return application_admin_key;
    }

    public static void reGenAdminKey()
    {
        application_admin_key = MacProvider.generateKey();
    }

    public static void reGenUserKey()
    {
        application_user_key = MacProvider.generateKey();
    }


    public static IAcsClient getAcsClient()
    {
        return acsClient;
    }

    public static void setAcsClient(IAcsClient acsClient)
    {
        DynamicValue.acsClient = acsClient;
    }

}

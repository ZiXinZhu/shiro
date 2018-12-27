package com.zzx.shiro.controller;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.bcel.internal.generic.GOTO;
import com.zzx.shiro.Emali.SendEmail;
import com.zzx.shiro.dao.UserDao;
import com.zzx.shiro.entity.UserEntity;
import com.zzx.shiro.jpush.JPushService;
import com.zzx.shiro.message_check.CheckSumBuilder;
import com.zzx.shiro.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Mr.John on 2018/11/23 17:49.
 **/

@RestController
@Slf4j
public class UserController {

    Map map = new HashMap();
    //发送验证码的请求路径URL
    private static final String
            SERVER_URL = "https://api.netease.im/sms/sendcode.action";
    //网易云信分配的账号，请替换你在管理后台应用下申请的Appkey
    private static final String
            APP_KEY = "c416a2286913396277416f1b382bd3b4";
    //网易云信分配的密钥，请替换你在管理后台应用下申请的appSecret
    private static final String APP_SECRET = "70dcb32a39b0";
    //随机数
    private static final String NONCE = "123456";
    //短信模板ID
    private static final String TEMPLATEID = "3973403";
    //手机号
    private static final String MOBILE="18781942015";
    //验证码长度，范围4～10，默认为4
    private static final String CODELEN = "6";


    @Autowired
    UserService userService;
    @Autowired
    JPushService jPushService;
    @Autowired
    UserDao userDao;
    @Autowired
    private JavaMailSender sender;
    @RequestMapping("/send")
    public void send() {
        new Thread(new SendEmail(sender,"1101648204@qq.com","863486267@qq.com","654321")).start();
    }
    @RequestMapping("/loginUser")
    public String loginUser(String userName, String password, HttpSession session) {
        return userService.login(userName, password, session);
    }

    @RequestMapping("/logOut")
    public String logOut(HttpSession session) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
//        session.removeAttribute("user");
        return "login";
    }


    //短信验证
    @PostMapping("/send")
    public void sendMS(@RequestParam("phone") String MOBILE) throws Exception {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(SERVER_URL);
            String curTime = String.valueOf((new Date()).getTime() / 1000L);
        /*
         * 参考计算CheckSum的java代码，在上述文档的参数列表中，有CheckSum的计算文档示例
         */
            String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET, NONCE, curTime);

            // 设置请求的header
            httpPost.addHeader("AppKey", APP_KEY);
            httpPost.addHeader("Nonce", NONCE);
            httpPost.addHeader("CurTime", curTime);
            httpPost.addHeader("CheckSum", checkSum);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

            // 设置请求的的参数，requestBody参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        /*
         * 1.如果是模板短信，请注意参数mobile是有s的，详细参数配置请参考“发送模板短信文档”
         * 2.参数格式是jsonArray的格式，例如 "['13888888888','13666666666']"
         * 3.params是根据你模板里面有几个参数，那里面的参数也是jsonArray格式
         */
            nvps.add(new BasicNameValuePair("templateid", TEMPLATEID));
            nvps.add(new BasicNameValuePair("mobile", MOBILE));
            nvps.add(new BasicNameValuePair("codeLen", CODELEN));

            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

            // 执行请求发短信啦
            HttpResponse response_app = httpClient.execute(httpPost);
            //获取code，msg，obj（验证码）

            map = (Map) JSONObject.parse(EntityUtils.toString(response_app.getEntity()));
            //String rePhone=MOBILE.substring(3,9);
            map.put("rePhone",MOBILE);
            System.out.println(EntityUtils.toString(response_app.getEntity(), "utf-8"));
        } catch (Exception e) {
            System.out.println("继续！");
        }
        /*
         * 1.打印执行结果，打印结果一般会200、315、403、404、413、414、500
         * 2.具体的code有问题的可以参考官网的Code状态表
         */
    }
    @RequestMapping("/register")
    public Object add(String phone, String password,int objs) {
        long start = System.currentTimeMillis();
        if (System.currentTimeMillis() - start > 1000 * 60) {
            map.remove("obj");
            map.remove("code");
        }
        try {
            if (((int) (map.get("obj"))) == (objs) && String.valueOf(map.get("rePhone")).equals(phone)) {
                int ch = userService.add(phone, password);
                if (ch == 1) {
                    return "成功";
                }
                return 0;
            }
            return 0;
        } catch (Exception e) {
            return "失败";
        }
    }


    @RequiresRoles("医生")
    @RequiresPermissions("1")
    @RequestMapping("/select")
    public Object select() {
        try {
            return userDao.getOne("aaa", "ea124fc9c09b8d8284e927e66b921760");
        }catch (Exception e){
            return "hello.html";
        }
    }

    @RequestMapping("/role")
    public Object role() {
        try {
            return userDao.getOne("aaa", "ea124fc9c09b8d8284e927e66b921760");
        }catch (Exception e){
            return "404.html";
        }
    }

    /**
     *
     * @param userId
     * @return 回调报错[1011]因为还没有推送目标
     * @throws ParseException
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @RequestMapping("/jpush")
    public boolean setms(String userId) throws ParseException, APIConnectionException, APIRequestException {
        String s="2018-12-12";
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date date=dateFormat.parse(s);
        return jPushService.withdrawSuccess(userId,date);
        //TODO  回调报错[1011]因为还没有推送目标
    }

    @RequestMapping("/page")
    public PageInfo<UserEntity> page(int pn){
        return userService.page(pn);
    }

    @GetMapping("/ip")
    public void getIp() throws UnknownHostException {
        InetAddress addr = InetAddress.getLocalHost();
        String ad=addr.getHostAddress().toString();
        log.info("IP:{}",ad);
        String name=addr.getHostName().toString();
        log.info("NAME:{}",name);
    }

}

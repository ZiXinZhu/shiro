package com.zzx.shiro.Emali;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * Created by Mr.John on 2018/12/19 22:02.
 **/
@Slf4j
public class SendEmail implements Runnable{

        private JavaMailSender sender;
        private String from;
        private String target;
        private String code;

    public SendEmail(JavaMailSender sender, String from, String target, String code)
        {
            this.sender = sender;
            this.from = from;
            this.target = target;
            this.code = code;
        }

        @Override
        public void run()
        {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(target);
            message.setSubject("猪猪侠");
            message.setText("您好，您的邮箱验证代码为:" + code);
            try
            {
                sender.send(message);
                log.info("发送成功！");
            } catch (Exception e)
            {
                log.info("发送失败！");
            }
        }
}

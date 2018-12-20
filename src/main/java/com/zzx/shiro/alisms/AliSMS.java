package com.zzx.shiro.alisms;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AliSMS implements Runnable
{
    private static Logger logger = LoggerFactory.getLogger("Mr.John");
    private String code;
    private String signName;
    private String templateCode;
    private String phoneNum;

    public AliSMS(String code, String phoneNum, String signName, String templateCode)
    {
        this.code = code;
        this.signName = signName;
        this.templateCode = templateCode;
        this.phoneNum = phoneNum;
    }

    @Override
    public void run()
    {
        SendSmsResponse sendSmsResponse = null;
        try
        {
            String parmedCode = null;
            SendSmsRequest request = new SendSmsRequest();
            request.setPhoneNumbers(phoneNum);
            request.setSignName(signName);
            request.setTemplateCode(templateCode);
            parmedCode = String.format("{\"code\":\"%s\"}", code);
            request.setTemplateParam(parmedCode);
            sendSmsResponse = DynamicValue.getAcsClient().getAcsResponse(request);
            if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK"))
                DynamicValue.addPhoneAndCode(phoneNum, code);
            else
                logger.error("请求短信验证失败 ： " + sendSmsResponse.getCode());
        } catch (ClientException e)
        {
            e.printStackTrace();
        }
    }
}

package com.muyi.mpdemo.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: muyi
 * @Date: Created in 10:25 2017/11/8
 * @Description:
 */

@Data
@ConfigurationProperties(prefix = "wechat")
public class WechatProperties {

    private Mp mp;
    private Server server;
    private Pay pay;
    private Open open;

    @Data
    public static class Mp{
        private String appId;
        private String appSecret;
    }
    @Data
    public static class Server{
        private String token;
        private String encodingAESKey;
        private String url;
    }

    @Data
    public static class Pay{
        //商户号
        private String mchId;
        //商户密钥
        private String mchKey;
        //商户证书路径
        private String keyPath;
        //微信支付异步通知地址
        private String notifyUrl;
    }
    @Data
    public static class Open{
        private String appId;
        private String appSecret;
    }

}



package com.muyi.mpdemo.config;

import com.muyi.mpdemo.config.properties.RedisProperties;
import com.muyi.mpdemo.config.properties.WechatProperties;
import com.muyi.mpdemo.wechat.handler.*;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.*;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Author: muyi
 * @Date: Created in 16:29 2017/11/7
 * @Description: 所有来自微信服务器的消息处理配置
 */

@Configuration
@ConditionalOnClass(WxMpService.class)
@EnableConfigurationProperties({WechatProperties.class, RedisProperties.class})
@Slf4j
public class WechatMpConfig {

    final String mingming = "oFqnmwmBEadJwBekobPqkPfo3qsk";

    @Autowired
    protected WechatProperties wechatProperties;
    @Autowired
    protected JedisPool jedisPool0;


    @Autowired
    protected LogHandler logHandler;
    @Autowired
    protected SubscribeHandler subscribeHandler;
    @Autowired
    protected UnsubcribeHandler unsubcribeHandler;
    @Autowired
    protected ScanHandler scanHandler;
    @Autowired
    protected MessageHandler messageHandler;

    @Bean
    public WxMpConfigStorage wxMpConfigStorage(){

        //WxMpInMemoryConfigStorage wxMpConfigStorage = new WxMpInMemoryConfigStorage();
        WxMpInRedisConfigStorage wxMpConfigStorage = new WxMpInRedisConfigStorage();

//        wxMpConfigStorage.setAppId(this.wechatProperties.getMp().getAppId());
//        wxMpConfigStorage.setSecret(this.wechatProperties.getMp().getAppSecret());

        wxMpConfigStorage.setAppId(this.wechatProperties.getMp().getAppId());
        wxMpConfigStorage.setSecret(this.wechatProperties.getMp().getAppSecret());

        wxMpConfigStorage.setAesKey(this.wechatProperties.getServer().getEncodingAESKey());
        wxMpConfigStorage.setToken(this.wechatProperties.getServer().getToken());

        wxMpConfigStorage.setJedis(jedisPool0.getResource());

        log.warn("wxMpConfigStorage initialized ...");
        return wxMpConfigStorage;
    }

    @Bean
    public WxMpService wxMpService(WxMpConfigStorage wxMpConfigStorage){
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage);
        log.warn("WxMpService initialized...");
        return wxMpService;
    }

    @Bean
    public WxMpMessageRouter router(WxMpService wxMpService){
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);

        //记录所有事件的日志（异步）
        newRouter.rule().async(true).handler(this.logHandler).next();

        //关注事件
        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.EVT_SUBSCRIBE).handler(this.subscribeHandler)
                .end();

        //取消关注事件
        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.EVT_UNSUBSCRIBE).handler(this.unsubcribeHandler)
                .end();

        //扫码事件
        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.EVT_SCAN).handler(scanHandler)
                .end();
        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.EVT_SCANCODE_WAITMSG).handler(scanHandler);

        //默认
        newRouter.rule().async(false).handler(this.messageHandler).end();



        return newRouter;
    }




}

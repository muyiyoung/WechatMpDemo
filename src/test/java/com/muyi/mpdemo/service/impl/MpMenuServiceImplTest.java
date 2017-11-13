package com.muyi.mpdemo.service.impl;

import com.muyi.mpdemo.exception.MpException;
import com.muyi.mpdemo.service.MpMenuService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Author: muyi
 * @Date: Created in 11:30 2017/11/13
 * @Description:
 */

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class MpMenuServiceImplTest {

    @Autowired
    private MpMenuService mpMenuService;

    @Autowired
    private WxMpService wxMpService;

    @Test
    public void createDefaultMenu() throws Exception {
        mpMenuService.createDefaultMenu();
    }

    @Test
    public void getMenu() throws Exception {
        log.info(wxMpService.getMenuService().menuGet().toJson());
    }

}
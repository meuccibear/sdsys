package com.adsys.service.system.app.impl;

import com.adsys.dao.DaoSupport;
import com.adsys.service.system.app.AppSendMessageManager;
import com.adsys.util.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("appSendMessageService")
public class AppSendMessageService implements AppSendMessageManager {
    @Resource(name = "daoSupport")
    private DaoSupport dao;

    /**新增
     * @param pd
     * @throws Exception
     */
    public void save(PageData pd)throws Exception{
        dao.save("AppSendMessageMapper.save", pd);
    }
}

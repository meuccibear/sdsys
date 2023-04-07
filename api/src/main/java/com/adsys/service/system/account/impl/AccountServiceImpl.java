package com.adsys.service.system.account.impl;

import com.adsys.dao.DaoSupport;
import com.adsys.dao.redis.RedisDao;
import com.adsys.entity.Page;
import com.adsys.service.system.account.AccountService;
import com.adsys.util.PageData;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("accountServiceImpl")
public class AccountServiceImpl implements AccountService {


    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Resource(name = "redisDaoImpl")
    private RedisDao redisDaoImpl;

    /**
     * 列表
     *
     * @param page
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<PageData> list(Page page) throws Exception {
        List<PageData> result = new ArrayList<PageData>();

        Number pagenum = (Number) dao.findForObject("AccountMapper.findPageCount", page);
        if (pagenum == null || pagenum.longValue() <= 0) {
            return result;
        }
        page.setTotalResult(pagenum.intValue());
        RowBounds rowbounds = new RowBounds(page.getOffset(), page.getShowCount());
        return (List<PageData>) dao.finPageForList("AccountMapper.userlistPage", page, rowbounds);
    }

    @Override
    public Object findByUsername(PageData pd) throws Exception {
        return (PageData) dao.findForObject("AccountMapper.findByUsername", pd);
    }

    @Override
    public void saveU(PageData pd) throws Exception {
        dao.save("AccountMapper.saveU", pd);
    }

    @Override
    public void editU(PageData pd) throws Exception {
        dao.save("AccountMapper.editU", pd);
    }

    @Override
    public PageData findById(PageData pd) throws Exception {
        return (PageData) dao.findForObject("AccountMapper.findById", pd);
    }

    @Override
    public void deleteU(PageData pd) throws Exception {
        dao.delete("AccountMapper.deleteU", pd);
    }

    @Override
    public List<PageData> getAccounts(PageData pd) throws Exception {
        List<PageData> pageDataList = (List<PageData>) dao.findForList("AccountMapper.findAvailableAccounts", pd);
        return pageDataList;
    }

    @Override
    public void bindingDevice(String did, String accounttype, String username) throws Exception {
        PageData bindingDevice = new PageData();
        bindingDevice.put("did", did);
        bindingDevice.put("accounttype", accounttype);
        bindingDevice.put("account", username);

        dao.update("AccountMapper.bindingDevice", bindingDevice);
    }

}

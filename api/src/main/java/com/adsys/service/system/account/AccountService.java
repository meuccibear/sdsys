package com.adsys.service.system.account;

import com.adsys.entity.Page;
import com.adsys.util.PageData;
import java.util.List;

public interface AccountService {

    List<PageData> list(Page page)throws Exception;

    Object findByUsername(PageData pd) throws Exception;

    void saveU(PageData pd) throws Exception;

    void editU(PageData pd) throws Exception;

    PageData findById(PageData pd) throws Exception;

    void deleteU(PageData pd) throws Exception;

    List<PageData> getAccounts(PageData pd) throws Exception;

    void bindingDevice(String did, String accounttype, String username) throws Exception;
}

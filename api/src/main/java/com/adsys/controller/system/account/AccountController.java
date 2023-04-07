package com.adsys.controller.system.account;

import com.adsys.common.enums.permissionEnum;
import com.adsys.controller.base.BaseController;
import com.adsys.entity.Page;
import com.adsys.service.system.account.AccountService;
import com.adsys.service.system.app.AppSendMessageManager;
import com.adsys.util.AuthorityUtil;
import com.adsys.util.Constants;
import com.adsys.util.DateUtil;
import com.adsys.util.PageData;
import com.adsys.util.json.JsonResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value = "/account")
public class AccountController extends BaseController {

    @Resource
    private AccountService accountService;

    @Resource(name = "appSendMessageService")
    private AppSendMessageManager appSendMessageManager;

    /**
     * 查询列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse toList() {
        try {
            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.App_User_READ);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            PageData pd = this.getPageData();
            pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
            if (pd.getString("keywords") != null && !"".equals(pd.getString("keywords")))
                pd.put("keywords", pd.getString("keywords"));
            Page page = new Page();
            page.setPd(pd);
            page.setOffset(pd.getString("offset") == null ? 0 : Integer.valueOf(pd.getString("offset")));
            List<PageData> result = accountService.list(page);
            for (PageData pageData : result) {
                pageData.remove("password");//把密码屏蔽
            }
            return ajaxSuccessPage("appuser", result, page, Constants.REQUEST_01, Constants.REQUEST_OK);
        } catch (Exception ex) {
            logger.info(ex.getMessage());
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }


    /**
     * 新增成员
     *
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse userMember() {
        try {

            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.App_User_WRITE);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            PageData pd = this.getPageData();
            if (null != pd && pd.size() > 0) {
                JsonResponse checkResult = userParamsCheck(pd);
                if (checkResult.isSuccess() == false) {
                    return checkResult;
                }

                pd.put("uid", this.get32UUID());
                pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
//                pd.put("accounttype", "appuser");
                pd.put("status", "正常");
                pd.put("adddate", DateUtil.getTime());
//                pd.put("password", new SimpleHash("SHA-1", pd.getString("username"), pd.getString("password")).toString());	//密码加密
                if (null == accountService.findByUsername(pd)) {    //判断用户名是否存在
                    accountService.saveU(pd);
                    return ajaxSuccess(Constants.REQUEST_01, Constants.REQUEST_OK);
                } else
                    return ajaxFailure(Constants.REQUEST_03, "用户已存在");
            } else {
                return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
            }

        } catch (Exception ex) {
            logger.info("add user error !!!");
            logger.info(ex.getMessage());
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    /**
     * 查询详情
     */
    @RequestMapping(value = "/appuser/{id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse findById(@PathVariable(value = "id") String id) {
        try {

            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.App_User_READ);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            if (null == id) {
                return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
            }
            PageData pd = new PageData();
            pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
            pd.put("aid", id);
            PageData pageData = accountService.findById(pd);
            pageData.put("password", "******");//把密码屏蔽
            return ajaxSuccess(pageData, Constants.REQUEST_01, Constants.REQUEST_OK);
        } catch (Exception ex) {
            logger.info(ex.getMessage());
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    /**
     * 修改信息
     */
    @RequestMapping(value = "/appuser/{id}", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse toupdate(@PathVariable("id") String id) {
        try {

            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.App_User_WRITE);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            PageData pd = this.getPageData();
            if (null == id) {
                return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
            }

            JsonResponse checkResult = userParamsCheck(pd);
            if (checkResult.isSuccess() == false) {
                return checkResult;
            }
            pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
            pd.put("aid", id);
            accountService.editU(pd);
            return ajaxSuccess(Constants.REQUEST_01, Constants.REQUEST_OK);
        } catch (Exception ex) {
            logger.info(ex.getMessage());
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/appuser/{id}", method = RequestMethod.DELETE, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse deleteById(@PathVariable("id") String id) {
        try {

            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.App_User_WRITE);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            if (null == id) {
                return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
            }
            PageData pd = new PageData();
            pd.put("aid", id);
            pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
            accountService.deleteU(pd);
            return ajaxSuccess(Constants.REQUEST_01, Constants.REQUEST_OK);
        } catch (Exception ex) {
            logger.info(ex.getMessage());
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    private JsonResponse userParamsCheck(PageData params) {
//        if(null == params.getString("username") || "".equals(params.getString("username"))){
//            return ajaxFailure(Constants.REQUEST_03, "请输入用户名");
//        }
//
//        if(null == params.getString("name") || "".equals(params.getString("name"))){
//            return ajaxFailure(Constants.REQUEST_03, "请输入姓名");
//        }
//
//        if(null == params.getString("tel") || "".equals(params.getString("tel"))){
//            return ajaxFailure(Constants.REQUEST_03, "请输入电话");
//        }
//
//        if(null == params.getString("roleid") || "".equals(params.getString("roleid"))){
//            return ajaxFailure(Constants.REQUEST_03, "请输入角色ID");
//        }


        return ajaxSuccess(Constants.REQUEST_01, Constants.REQUEST_OK);
    }

}

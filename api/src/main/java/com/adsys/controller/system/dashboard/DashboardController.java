package com.adsys.controller.system.dashboard;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.adsys.controller.base.BaseController;
import com.adsys.service.system.dashboard.DashboardManager;
import com.adsys.util.AuthorityUtil;
import com.adsys.util.Constants;
import com.adsys.util.PageData;
import com.adsys.util.json.JsonResponse;


@Controller
@RequestMapping(value = "/dashboard")
public class DashboardController extends BaseController {

    @Resource(name = "dashboardService")
    private DashboardManager dashboardService;


    /**
     * 控制板信息
     */
    @RequestMapping(value = "/dashboardinfo", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse dashboardinfo() {
        try {
//			boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Customer_READ);
//			if (auth == false)
//				return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);
            PageData pd = new PageData();
            pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
            PageData result = dashboardService.getDashboardInfo(pd);
            return ajaxSuccess(result, Constants.REQUEST_01, Constants.REQUEST_OK);
//			return null;
        } catch (Exception ex) {
            ex.printStackTrace();
//			logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }


}

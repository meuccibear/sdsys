package com.adsys.controller.system.group;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.adsys.util.*;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.adsys.common.enums.permissionEnum;
import com.adsys.controller.base.BaseController;
import com.adsys.entity.Page;
import com.adsys.service.system.customer.CustomerManager;
import com.adsys.service.system.device.impl.DeviceService;
import com.adsys.service.system.group.GroupManager;
import com.adsys.service.system.log.LogManager;
import com.adsys.util.ElTreeBuilder.Node;
import com.adsys.util.json.JsonResponse;
import com.alibaba.fastjson.JSONArray;


@Controller
@RequestMapping(value = "/groups")
public class GroupController extends BaseController {

    @Resource(name = "groupService")
    private GroupManager groupsservice;

    @Resource(name = "deviceService")
    private DeviceService deviceservice;

    /**
     * 新增接口
     *
     * @return
     */
    @RequestMapping(value = "/group", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse addGroup() {
        try {

            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Group_EDIT);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            PageData pd = this.getPageData();
            if (null != pd && pd.size() > 0) {
                JsonResponse checkResult = groupsParamsCheck(pd);
                if (checkResult.isSuccess() == false) {
                    return checkResult;
                }
                if (!"0".equals(pd.getString("gpid"))) {
                    PageData parma = new PageData();
                    parma.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                    parma.put("gid", pd.getString("gpid"));
                    PageData pdata = groupsservice.findById(parma);
                    if (pdata != null) {
                        pd.put("gpid", pdata.getString("gpid") + "," + String.valueOf((int) pdata.get("gid")));
                        pd.put("gpath", pdata.getString("gpath") + "/" + pdata.getString("gname"));
                    } else
                        return ajaxFailure(Constants.REQUEST_03, "不存在此父节点");
                } else
                    pd.put("gpath", "/");
                pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                groupsservice.save(pd);
                return ajaxSuccess(Constants.REQUEST_01, Constants.REQUEST_OK);

            } else {
                return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
            }

        } catch (Exception ex) {
            logger.info("add group error !!!");
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    /**
     * 查询纠纷列表
     */
    @RequestMapping(value = "/groups", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse productList() {
        try {
            PageData pd = this.getPageData();
            Page page = new Page();
            pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
            if (pd.getString("keywords") != null && !"".equals(pd.getString("keywords"))) {
                pd.put("keywords", pd.getString("keywords"));
            }
            page.setPd(pd);
            page.setOffset(pd.getString("offset") == null ? 0 : Integer.valueOf(pd.getString("offset")));
            List<PageData> result = groupsservice.list(page);
            return ajaxSuccessPage("group", result, page, Constants.REQUEST_01, Constants.REQUEST_OK);
        } catch (Exception ex) {
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }


    /**
     * 查询详情
     */
    @RequestMapping(value = "/group/{id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse findGroupById(@PathVariable("id") String id) {
        try {
            PageData pd = this.getPageData();

            pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
            pd.put("gid", id);
            PageData pageData = groupsservice.findById(pd);

            return ajaxSuccess(pageData, Constants.REQUEST_01, Constants.REQUEST_OK);
        } catch (Exception ex) {
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    /**
     * 修改群组信息
     */
    @RequestMapping(value = "/group/{id}", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse updateGroup(@PathVariable("id") String id) {
        try {

            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Group_EDIT);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            PageData pd = this.getPageData();
            if (null == id) {
                return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
            }
            if (!"0".equals(pd.getString("gpid"))) {
                PageData parma = new PageData();
                parma.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                parma.put("gid", pd.getString("gpid"));
                PageData pdata = groupsservice.findById(parma);
                if (pdata != null) {
                    pd.put("gpid", pdata.getString("gpid") + "," + String.valueOf((int) pdata.get("gid")));
                    pd.put("gpath", pdata.getString("gpath") + "/" + pdata.getString("gname"));
                } else
                    return ajaxFailure(Constants.REQUEST_03, "不存在此父节点");
            } else
                pd.put("gpath", "/");
            pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
            groupsservice.edit(pd);
            return ajaxSuccess(Constants.REQUEST_01, Constants.REQUEST_OK);
        } catch (Exception ex) {
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }


    void getCh(List<PageData> data, Integer pid) {
        List<PageData> result = new ArrayList<>();
        for (PageData pageData : data) {
            if (pageData.getString("gpid").equals(pid.toString())) {

            }
        }
    }

    /**
     * 查询分组，树状结构返回
     */
    @RequestMapping(value = "/tree", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse getTree() {
        try {
            List<TreeBuilder.Node> groupNodes = new ArrayList<TreeBuilder.Node>();
            PageData pd = new PageData();
            pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
            pd.put("gpid", "0");

            List<PageData> result = groupsservice.listAll(pd);
            for (int i = 0; i < result.size(); i++) {

                PageData pd_i = result.get(i);
                if (pd_i.getString("gpid").equals("0")) {
                    TreeBuilder.Node root = new TreeBuilder.Node(String.valueOf((int) pd_i.get("gid")), pd_i.getString("gpid"), pd_i.getString("gname"));
                    groupNodes.add(root);
                } else {
                    String plist[] = pd_i.getString("gpid").split(",");
                    TreeBuilder.Node node = new TreeBuilder.Node(String.valueOf((int) pd_i.get("gid")), plist[plist.length - 1], pd_i.getString("gname"));
                    groupNodes.add(node);
                }
            }
            PageData rst = new PageData();
            TreeBuilder treeBuilder = new TreeBuilder(groupNodes);
            rst.put("tree", treeBuilder.buildJSONTree());
            return ajaxSuccess(rst, Constants.REQUEST_01, Constants.REQUEST_OK);
        } catch (Exception ex) {
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    @RequestMapping(value = "/tree2", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse getTree2() {
        try {
            List<ElTreeBuilder.Node> groupNodes = new ArrayList<ElTreeBuilder.Node>();
            PageData pd = new PageData();
            pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
            pd.put("gpid", "0");

            List<PageData> result = groupsservice.listAll(pd);

            for (int i = 0; i < result.size(); i++) {

                PageData pd_i = result.get(i);
                if (pd_i.getString("gpid").equals("0")) {
                    Node root = new Node((int) pd_i.get("gid"), Integer.valueOf(pd_i.getString("gpid")), pd_i.getString("gname"));
                    groupNodes.add(root);
                } else {
                    String plist[] = pd_i.getString("gpid").split(",");
                    Node node = new Node((int) pd_i.get("gid"), Integer.valueOf(plist[plist.length - 1]), pd_i.getString("gname"));
                    groupNodes.add(node);
                }
            }
//            logger.info("groupNodes:" + JSON.toJSONString(groupNodes));
            List<PageData> devices = deviceservice.findDeviceByGid(null);
            PageData rst = new PageData();
            rst.put("tree", JSON.toJSONString(getNodeById(devices, groupNodes, 0)).toString());

            return ajaxSuccess(rst, Constants.REQUEST_01, Constants.REQUEST_OK);
        } catch (Exception ex) {
//            ex.printStackTrace();
//            logger.error(ex);
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    List<ElTreeBuilder.NodeBuild> getNodeById(List<PageData> devices, List<ElTreeBuilder.Node> groupNodes, Integer id) {
        List<ElTreeBuilder.NodeBuild> nodeBuilds = new ArrayList<>();
        for (Node rootNode : groupNodes) {
            if (rootNode.getPid() == id) {
                List<ElTreeBuilder.NodeBuild> children = getNodeById(devices, groupNodes, rootNode.getId());
//                logger.info("children:" + JSON.toJSONString(children) + (null == children || children.size() == 0));
                if (children.size() == 0) {
                    try {
                        for (PageData sonDevice : devices) {
                            if (sonDevice.get("gid") == rootNode.getId()) {
                                children.add(new ElTreeBuilder.NodeBuild(String.format("device_%s", sonDevice.getString("did")), sonDevice.getString("dname"), null));
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                nodeBuilds.add(toNodeBuild(rootNode, children));
            }
        }
        return nodeBuilds;
    }

    ElTreeBuilder.NodeBuild toNodeBuild(Node node, List children) {
        ElTreeBuilder.NodeBuild nodeBuild = new ElTreeBuilder.NodeBuild(String.format("group_%d", node.getId()), node.getLabel(), null == children || children.size() == 0 ? null : children);
        return nodeBuild;
    }


    /**
     * 删除纠纷
     */
    @RequestMapping(value = "/group/{id}", method = RequestMethod.DELETE, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse deleteById(@PathVariable("id") String id) {
        try {

            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Group_EDIT);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            if (null == id) {
                return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
            }

            PageData parma = new PageData();
            parma.put("gid", id);
            parma.put("ugid", null);
            deviceservice.updateGid(parma);
//            List<PageData> devices = deviceservice.findDeviceByGid(id);
//            if (devices.size() > 0) {
//                return ajaxFailure(Constants.REQUEST_05, "该分组下有设备，请先删除设备");
//            }

            List<PageData> groups = groupsservice.findGroupByGpid(id);
            if (groups.size() > 0) {
                for (PageData group : groups) {
                    List<PageData> sonDevices = deviceservice.findDeviceByGid(String.valueOf((int) group.get("gid")));
                    if (sonDevices.size() > 0) {
                        return ajaxFailure(Constants.REQUEST_05, "该分组下的【" + group.getString("gname") + "】分组有设备，请先删除设备");
                    }
                }
                for (PageData group : groups) {
                    groupsservice.delGroupByGid(String.valueOf((int) group.get("gid")));
                }
            }
            groupsservice.delGroupByGid(id);

            return ajaxSuccess(Constants.REQUEST_01, Constants.REQUEST_OK);
        } catch (Exception ex) {
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    private JsonResponse groupsParamsCheck(PageData params) {

        if (null == params.getString("gpid") || "".equals(params.getString("gpid"))) {
            return ajaxFailure(Constants.REQUEST_03, "请输入父ID");
        }

        if (null == params.getString("gname") || "".equals(params.getString("gname"))) {
            return ajaxFailure(Constants.REQUEST_03, "请输入群组名称");
        }

        return ajaxSuccess(Constants.REQUEST_01, Constants.REQUEST_OK);
    }

}

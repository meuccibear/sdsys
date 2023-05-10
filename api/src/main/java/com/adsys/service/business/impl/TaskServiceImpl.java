package com.adsys.service.business.impl;

import com.adsys.dao.redis.RedisDao;
import com.adsys.service.adEditor.program.impl.AdProgramService;
import com.adsys.service.business.TaskService;
import com.adsys.service.system.customer.CustomerManager;
import com.adsys.service.system.device.ext.impl.DeviceExtService;
import com.adsys.service.system.device.impl.DeviceService;
import com.adsys.service.system.device.timepref.impl.DeviceTimePrefService;
import com.adsys.service.system.group.GroupManager;
import com.adsys.service.system.log.impl.LogService;
import com.adsys.util.DateUtil;
import com.adsys.util.PageData;
import com.adsys.util.PlayingMonCache;
import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("taskService")
public class TaskServiceImpl implements TaskService {

    private Logger logger = Logger.getLogger(this.getClass());


    @Resource(name = "deviceService")
    private DeviceService deviceservice;

    @Resource(name = "deviceExtService")
    private DeviceExtService deviceextservice;

    @Resource(name = "logService")
    private LogService logservice;

    @Resource(name = "groupService")
    private GroupManager groupsservice;

    @Resource(name = "customerService")
    private CustomerManager customerService;

    @Resource(name = "deviceTimePrefService")
    private DeviceTimePrefService devicetimeprefservice;

    @Resource(name = "adProgramService")
    private AdProgramService adProgramservice;

    @Resource(name = "redisDaoImpl")
    private RedisDao redisDaoImpl;

    @Override
    public void checkDStatus() {
        try {
            List<String> offlines = new ArrayList<>();
            List<String> onlines = new ArrayList<>();

            List<PageData> result = deviceservice.devices();
            for (PageData pd_i : result) {
                String did = pd_i.getString("did");
                Map<String, String> map = redisDaoImpl.getMap(did);
//                logger.info("map:" + JSON.toJSONString(map));
                if (null != map && map.containsKey("time")) {
                    int mins = DateUtil.getDiffMin(map.get("time"), DateUtil.getTime());
                    if (mins > 1 * (1000 * 60) && "online".equals(pd_i.getString("dstatus"))) {
                        onlines.add(pd_i.getString("did"));
                    } else {
                        offlines.add(pd_i.getString("did"));
                    }
                } else {
                    offlines.add(pd_i.getString("did"));
                }
//                String beatdate = DateUtil.formatSdfTimes((Timestamp) pd_i.get("beatdate"));
//                // 判断心跳是否超时，如果超时将状态设为离线
//                int mins = DateUtil.getDiffMin(beatdate, DateUtil.getTime());
//                if (mins > 1 * (1000 * 60) && "online".equals(pd_i.getString("dstatus"))) {
//                    offlines.add(pd_i.getString("did"));
//                } else {
//                    onlines.add(pd_i.getString("did"));
//                }
            }

            PageData pageData = new PageData();
            if (offlines.size() > 0) {
                pageData.put("dids", offlines);
                pageData.put("dstatus", "offline");
                deviceservice.updateStates(pageData);
            }

            if (onlines.size() > 0) {
                pageData.put("dids", onlines);
                pageData.put("dstatus", "online");
                deviceservice.updateStates(pageData);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

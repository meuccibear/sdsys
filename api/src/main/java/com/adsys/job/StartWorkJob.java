package com.adsys.job;

import com.adsys.service.business.TaskService;
import com.adsys.service.system.device.impl.DeviceService;
import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;

/**
 * 定时器
 *
 */
public class StartWorkJob {
    TaskService taskService;

    public void startWork(){
        taskService = (TaskService) ContextLoader.getCurrentWebApplicationContext().getBean("taskService");
        taskService.checkDStatus();
    }
}

package com.adsys.task;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.adsys.util.Logger;
import com.adsys.util.PageData;

@Component
public class mainTask {
    
	protected Logger logger = Logger.getLogger(this.getClass());
	
	
    @Scheduled(cron = "0 0 2 * * ?")
        void queueTask(){
        
            new Thread(new Runnable() {  
                @Override  
                public void run() {  

                }  
            }).start();  

    }
}

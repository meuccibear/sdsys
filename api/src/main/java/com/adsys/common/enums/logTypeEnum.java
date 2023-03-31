package com.adsys.common.enums;

public enum logTypeEnum {
	INFO("记录"), 
	FAIL("错误"), 
	WARNING("警告"), 
	DANGER("危险");  

    private String name;  
 
    private logTypeEnum(String name) {  
        this.name = name;  
    }  

    @Override  
    public String toString() {  
        return this.name;  
    }  
}

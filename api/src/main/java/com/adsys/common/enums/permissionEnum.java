package com.adsys.common.enums;

public enum permissionEnum {
	
	//100 用户管理  查看/ 新增，编辑，删除
	Sys_User_WRITE("101", "用户添加编辑删除"),
	Sys_User_READ("102", "用户查看"),
	Sys_Role_WRITE("103", "角色添加编辑删除"),
	Sys_Role_READ("104", "角色查看"),
	App_User_WRITE("105", "会员添加编辑删除"),
	App_User_READ("106", "会员查看"),
	
	//200 客户管理 编辑，删除
	Customer_EDIT("201", "客户编辑删除"),
	Customer_READ("202", "客户查看"),
    
    //300 设备管理 新增，编辑，删除，查看
    Device_READ("301", "设备查看"),
    Device_EDIT("302", "设备编辑删除"),
    
    //400 群组管理 编辑
    Group_EDIT("401", "群组编辑删除"),
    
    //500 文件管理 
    File_MANAGE("501", "文件管理"),
    
    //700 节目管理 编辑，删除，查看
    Program_EDIT("702", "节目编辑删除"),
    Program_REVIEW("701", "节目审核"),
    Program_PUSH("703", "节目发布"),
    
    //800 系统设置管理 查看
    Setting_WRITE("801", "系统设置权限"),
    Setting_UPDATE("802", "apk更新权限"),
    
    Setting_APKUPLOAD("901", "apk上传权限"),
    ;

    /* 编码 */
    private String code;

    /* 描述 */
    private String description;

    permissionEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String code() {
        return code;
    }

    public String description() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // 普通方法
    public static String getDescription(String code) {
        for (permissionEnum en : permissionEnum.values()) {
            if (en.getCode().equals(code)) {
                return en.getDescription();
            }
        }
        return null;
    }
}

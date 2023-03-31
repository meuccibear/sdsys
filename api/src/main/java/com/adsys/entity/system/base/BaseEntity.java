package com.adsys.entity.system.base;

import java.io.Serializable;
import java.util.Date;

/**
 * Base实体类
 */
public class BaseEntity implements Serializable{

    private String createUser;//创建用户
    private String updateUser;//最后更新用户
    private Date createTime;//创建时间
    private Date updateTime;//最后更新时间

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}

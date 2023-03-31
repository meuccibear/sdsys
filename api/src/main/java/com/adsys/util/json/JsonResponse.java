package com.adsys.util.json;

import java.util.Map;

/**
 * Ajax请求返回结果类
 * 
 * Ajax请求时，统一返回该类
 *
 *
 */
public class JsonResponse {
    /** 成功标识位 */
    private boolean success;
    /** 消息 */
    private String msg;
    /** 后台验证错误消息列表 */
    private Map<String, String> errors;
    /** 返回的数据结果 */
    private Object data;
    /** 返回状态码
     * 00 请求失败
     * 01 请求成功
     * 02 返回控制
     * 03 请求参数不完整
     * 04 用户名或密码错误
     * 05 系统错误
     * */
    private Integer status;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

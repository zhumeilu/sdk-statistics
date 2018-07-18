package com.yishen.sdk.bean;

import lombok.Data;

/**
 * Created by zhumeilu on 2018/5/7.
 */
@Data
public class AjaxResult {
    private Integer code;
    private boolean success;
    private String msg ;
    private Integer count;
    private Object data;

    public AjaxResult(){

    }
    public AjaxResult(boolean success,String msg){
        this.success = success;
        this.msg = msg;
    }
}

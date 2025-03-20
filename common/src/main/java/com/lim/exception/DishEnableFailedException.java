package com.lim.exception;

/**
 * 套餐启用失败异常
 */
public class DishEnableFailedException extends BaseException {

    public DishEnableFailedException(){}

    public DishEnableFailedException(String msg){
        super(msg);
    }
}

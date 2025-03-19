package com.lim.handler;

import com.lim.exception.BaseException;
import com.lim.exception.DeletionNotAllowedException;
import com.lim.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(BaseException.class)
    public Result exceptionHandler(BaseException ex) {
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    //这样，如果发生对应类型的异常则会将这个异常类传入所修饰方法对应的异常类中
    //Unique Column 约束异常
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        e.printStackTrace();
        if (e.getMessage().contains("Duplicate entry")) {
            //log.info("errorMsg:{}",e.getMessage());
            return Result.error(e.getMessage().split(" ")[2] + "已存在");
        }
        return Result.error("未知错误");
    }

    @ExceptionHandler(DeletionNotAllowedException.class)
    public Result handleDeletionNotAllowedException(DeletionNotAllowedException e){
        return Result.error(e.getMessage());
    }

}

package com.lim.aop;

import com.lim.anno.AutoFill;
import com.lim.constant.AutoFillConstant;
import com.lim.context.BaseContext;
import com.lim.enumeration.OperationType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Component
@Aspect
public class AutoFillAspect {

    @Before("@annotation(com.lim.anno.AutoFill)")
    public void autoFill(JoinPoint joinPoint){
        //aop获得方法签名Signature，强转为含有能获得方法对象的MethodSignature
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //反射获得取得@AutoFill所修饰的方法对象
        Method method = methodSignature.getMethod();
        //解析注解，获得指定的该方法上所修饰注解及其的值
        AutoFill autoFill = method.getDeclaredAnnotation(AutoFill.class);
        OperationType type = autoFill.type();

        //约定@Auto修饰的方法第一个对象为需要自动填充字段值的对象
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length ==0){
            return;
        }
        Object entity = args[0];

        //要填充的值
        LocalDateTime now = LocalDateTime.now();
        Long userId = BaseContext.getCurrentId();

        if(type == OperationType.INSERT){
            try {
                //反射获取设置方法对象
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);

                //invoke设值
                setCreateTime.invoke(entity,now);
                setUpdateTime.invoke(entity,now);
                setCreateUser.invoke(entity,userId);
                setUpdateUser.invoke(entity,userId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (type == OperationType.UPDATE){
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                setUpdateUser.invoke(entity,userId);
                setUpdateTime.invoke(entity,now);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
}

package com.stylefeng.guns.core.aop;

import com.stylefeng.guns.core.shiro.ShiroKit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Aspect
@Component
public class CurUserAspect {

    @Pointcut("execution(* com.stylefeng.guns.modular.support.service..*.*Aop(..))")
    public void aspectExecute() {
    }

    @Around("aspectExecute()")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] param =  proceedingJoinPoint.getArgs();
        String userId = ShiroKit.getUser().getId();
        List<String> roleNames = ShiroKit.getUser().getRoleNames();
        for (String name : roleNames) {
            if (name.equals("高级管理员") || name.equals("外网")) {
                userId = null;//新的业务是rwrecinfo表的CURRECLOCKUSERNUM字段存放着userId,代表着权限，分派任务的时候这个值是null，等待分配任务后记录id（相当于记录了权限）
                break;
            }
        }
        ((HashMap)param[0]).put("userId",userId);
        return proceedingJoinPoint.proceed(param);
    }

}

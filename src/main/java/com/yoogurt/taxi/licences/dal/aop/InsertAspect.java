package com.yoogurt.taxi.licences.dal.aop;

import com.yoogurt.taxi.licences.dal.annotation.Domain;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 为Mapper的插入方法设置切面，设置Bean中的公共字段值。
 */
@Slf4j
@Aspect
@Order(1)
@Component
public class InsertAspect {

    @Before("execution(* com.yoogurt.taxi.licences.dal.mapper..*..insert*(..)) || " +
            "execution(* com.yoogurt.taxi.licences.dal.mapper..*..add*(..)) || " +
            "execution(* com.yoogurt.taxi.licences.dal.mapper..*..save*(..)) ")
    public void before(JoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg != null) {
                if (arg instanceof List) {
                    List<?> argList = (List) arg;
                    if (!CollectionUtils.isEmpty(argList)) {
                        for (Object object : argList) {
                            domainHandle(object);
                        }
                    }
                } else {
                    domainHandle(arg);
                }
            }
        }
    }

    private void domainHandle(Object object) throws Exception {
        //添加了@Domain注解
        if (object.getClass().isAnnotationPresent(Domain.class)) {
            try {//无特殊情况，以下五个字段一定会出现在Bean内
                MethodUtils.invokeMethod(object, "setIsDeleted", Boolean.FALSE);
                MethodUtils.invokeMethod(object, "setGmtCreate", new Date());
                MethodUtils.invokeMethod(object, "setGmtModify", new Date());
            } catch (Exception e) {
                log.error("设置公共字段失败,{}", e);
            }

        }
    }
}

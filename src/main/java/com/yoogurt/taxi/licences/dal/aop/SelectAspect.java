package com.yoogurt.taxi.licences.dal.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 默认过滤逻辑删除的记录，如果手动设置了is_deleted，以手动设置的值为准。
 */
@Slf4j
@Aspect
@Component
public class SelectAspect {

    @Before("execution(* com.yoogurt.taxi.licences.dal.mapper..*..select*ByExample(..))")
    public void before(JoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        try {
            if (args != null && args.length > 0 && args[0] instanceof Example) {
                Example ex = (Example) args[0];
                List<Example.Criteria> criteriaList = ex.getOredCriteria();
                if (CollectionUtils.isNotEmpty(criteriaList)) {
                    for (Example.Criteria criteria : criteriaList) {
                        List<Example.Criterion> criterionList = criteria.getCriteria();
                        for (Example.Criterion criterion : criterionList)
                            if (criterion.getCondition().contains("is_deleted")) return;
                    }
                    criteriaList.get(0).andEqualTo("isDeleted", Boolean.FALSE);
                } else {
                    ex.createCriteria().andEqualTo("isDeleted", Boolean.FALSE);
                }
            }
        } catch (Exception e) {
            log.error("注入is_deleted出现异常，{}", e);
        }
    }
}


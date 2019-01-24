package org.benetech.servicenet.service.annotation;

import org.apache.commons.lang3.BooleanUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.benetech.servicenet.domain.AbstractEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ConfidentialFilterAspect {

    @Around("@annotation(ConfidentialFilter)")
    @SuppressWarnings("checkstyle:IllegalThrows")
    public Object confidentialFilter(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] methodParams = joinPoint.getArgs();
        if (isNotConfidential(methodParams)) {
            return joinPoint.proceed();
        }
        return null;
    }

    private boolean isNotConfidential(Object[] methodParams) {
        boolean result = true;

        for (Object param : methodParams) {
            if (param instanceof AbstractEntity) {
                if (BooleanUtils.isTrue(((AbstractEntity) param).getIsConfidential())) {
                    result = false;
                }
            }
        }
        return result;
    }
}

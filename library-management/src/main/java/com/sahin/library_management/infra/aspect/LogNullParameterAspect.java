package com.sahin.library_management.infra.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
@ConditionalOnExpression("${aspect.enabled.log-null-parameters}")
public class LogNullParameterAspect {

    @Pointcut("within(com.sahin.library_management..*)")
    private void pointCut() {}

    @Before("pointCut()")
    public void logNullParameter(JoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] arguments = joinPoint.getArgs();
        Method method = signature.getMethod();

        for (Object arg : arguments) {
            if (arg == null) {
                log.debug("NULL PARAMETER! " + method.getDeclaringClass().getSimpleName() + "." + method.getName() + " has a null value!");
            }
        }
    }
}

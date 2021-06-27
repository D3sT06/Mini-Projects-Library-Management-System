package com.sahin.lms.infra.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
@Slf4j
@ConditionalOnExpression("${aspect.enabled.log-execution-time}")
public class LogExecutionAspect {

    @Pointcut("execution(public * *(..))")
    private void publicMethodPointcut() {}

    @Pointcut("within(@com.sahin.lms.infra.annotation.LogExecutionTime *)")
    private void beanAnnotatedWithLogExecutionTime() {}

    @Around("publicMethodPointcut() && beanAnnotatedWithLogExecutionTime()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        long startTime = System.currentTimeMillis();

        String authenticatedBy = "NULL AUTH";
        if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            authenticatedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        }

        log.debug(method.getDeclaringClass().getSimpleName() + "." + method.getName() + " executed" +
                " by " + authenticatedBy +
                " at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + " in local time.");

        Object object = joinPoint.proceed();

        long endTime = System.currentTimeMillis();

        log.debug(method.getDeclaringClass().getSimpleName() + "." + method.getName() + " took " + (endTime-startTime) + "ms for execution");

        return object;
    }
}

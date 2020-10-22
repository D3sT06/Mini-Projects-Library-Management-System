package com.sahin.library_management.infra.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
@Slf4j
public class LogExecutionAspect {

    @Pointcut("execution(public * *(..))")
    private void publicMethodPointcut() {}

    @Pointcut("within(@com.sahin.library_management.infra.annotation.LogExecutionTime *)")
    private void beanAnnotatedWithLogExecutionTime() {}

    @Around("publicMethodPointcut() && beanAnnotatedWithLogExecutionTime()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        log.debug(method.getDeclaringClass().getSimpleName() + "." + method.getName() + " executed at " + LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + " in local time."
        );

        return joinPoint.proceed();
    }
}

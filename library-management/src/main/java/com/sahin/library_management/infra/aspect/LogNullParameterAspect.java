package com.sahin.library_management.infra.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class LogNullParameterAspect {

    @Pointcut("execution(public * *(..))")
    private void pointCut() {}

    @Around("pointCut()")
    public Object logNullParameter(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] arguments = joinPoint.getArgs();
        Method method = signature.getMethod();

        for (Object arg : arguments) {
            if (arg == null) {
                log.debug("Watch out! " + method.getDeclaringClass().getSimpleName() + "." + method.getName() + " has a null value!");
            }
        }
        return joinPoint.proceed();
    }
}

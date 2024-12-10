package com.example.library.management.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // Pointcut to capture methods in the service layer for book and patron transactions
    @Pointcut("execution(* com.example.library.management.service.*.*(..))")
    public void serviceMethods() {}

    // Before method: Log method entry
    @Before("serviceMethods()")
    public void logMethodEntry(JoinPoint joinPoint) {
        logger.info("Entering method: " + joinPoint.getSignature().getName() + " with arguments: " + joinPoint.getArgs());
    }

    // After method: Log method exit
    @After("serviceMethods()")
    public void logMethodExit(JoinPoint joinPoint) {
        logger.info("Exiting method: " + joinPoint.getSignature().getName());
    }

    // After method successfully returns: Log return value
    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logMethodReturn(JoinPoint joinPoint, Object result) {
        logger.info("Method " + joinPoint.getSignature().getName() + " returned with value: " + result);
    }

    // After method throws exception: Log the exception
    @AfterThrowing(pointcut = "serviceMethods()", throwing = "exception")
    public void logMethodException(JoinPoint joinPoint, Exception exception) {
        logger.error("Method " + joinPoint.getSignature().getName() + " threw exception: " + exception.getMessage());
    }

    // Around method: Capture execution time
    @Around("serviceMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {  // Use ProceedingJoinPoint
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Object result = joinPoint.proceed();  // Now you can call proceed()
            stopWatch.stop();
            logger.info("Execution time for method " + joinPoint.getSignature().getName() + ": " + stopWatch.getTotalTimeMillis() + " ms");
            return result;
        } catch (Throwable throwable) {
            stopWatch.stop();
            logger.error("Execution failed for method " + joinPoint.getSignature().getName() + ": " + throwable.getMessage());
            throw throwable;
        }
    }
}

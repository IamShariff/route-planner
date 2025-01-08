package com.metro.routeplanner.util;

import java.time.Duration;
import java.time.LocalDateTime;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class is used to log total time, method arguments, and responses of different methods.
 * It ensures the method's execution time is logged even if an exception occurs.
 * 
 * @author mdsharif
 */
@Component
@Aspect
public class LoggerService {

    private static final Logger log = LoggerFactory.getLogger(LoggerService.class);

    @Around("@annotation(LogTime)")
    public Object checkTime(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime startTime = LocalDateTime.now();
        log.info("In method " + joinPoint.getSignature() + " starts at " + startTime);
        Object result = null;
        try {
            result = joinPoint.proceed();
        } finally {
            LocalDateTime endTime = LocalDateTime.now();
            Duration duration = Duration.between(startTime, endTime);
            long seconds = duration.getSeconds();
            log.info("In method " + joinPoint.getSignature() + " ends at " + endTime);
            log.info("Total time taken by method " + joinPoint.getSignature().getName() + " is " + seconds + " seconds.");
        }
        return result;
    }

    @Around("@annotation(LogRequest)")
    public Object checkRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("In method " + joinPoint.getSignature() + " with request: " 
                + objectMapper.writeValueAsString(joinPoint.getArgs()));
        return joinPoint.proceed();
    }

    @Around("@annotation(LogResponse)")
    public Object checkResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("In method " + joinPoint.getSignature() + " with response: " 
                + objectMapper.writeValueAsString(result));
        return result;
    }
}
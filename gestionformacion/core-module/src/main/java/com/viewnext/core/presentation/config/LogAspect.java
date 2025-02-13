package com.viewnext.core.presentation.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ch.qos.logback.classic.Logger;

@Aspect
@Component
public class LogAspect {
	
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(LogAspect.class);

    @Around("execution( package com.viewnext.course.presentation.controller.*(..))")
    public Object controller(ProceedingJoinPoint joinPoint) throws Throwable {
    	
    	long startTime = System.currentTimeMillis();
    	Object result = joinPoint.proceed();
    	long endTime = System.currentTimeMillis();
    	long executionTime = endTime - startTime;
    	
    	LOGGER.info("Método {} ejecutado en {} ms", joinPoint.getSignature().getName(), executionTime);
    	LOGGER.info("Entrando en el método {}", joinPoint.getSignature().getName());
    	LOGGER.info("Saliendo del método {}", joinPoint.getSignature().getName());

    	return result;
    	
    }
    
}

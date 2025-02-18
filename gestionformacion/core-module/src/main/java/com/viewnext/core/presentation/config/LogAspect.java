package com.viewnext.core.presentation.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import ch.qos.logback.classic.Logger;

@Aspect
@Component
@EnableAspectJAutoProxy 
public class LogAspect {
	
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(LogAspect.class);

    @Around("execution(* com.viewnext.login.presentation.controller..*(..))")
    public Object loginLogController(ProceedingJoinPoint joinPoint) throws Throwable {
    	
    	long startTime = System.currentTimeMillis();
    	Object result = joinPoint.proceed();
    	long endTime = System.currentTimeMillis();
    	long executionTime = endTime - startTime;
    	
    	LOGGER.info("******************************************************************");
    	LOGGER.info("Método {} ejecutado en {} ms", joinPoint.getSignature().getName(), executionTime);
    	LOGGER.info("Entrando en el método {}", joinPoint.getSignature().getName());
    	LOGGER.info("Saliendo del método {}", joinPoint.getSignature().getName());
    	LOGGER.info("******************************************************************");
    	
    	return result;
    	
    }
    
    @Around("execution(* com.viewnext.course.presentation.controller..*(..))")
    public Object courseLogController(ProceedingJoinPoint joinPoint) throws Throwable {
    	
    	long startTime = System.currentTimeMillis();
    	Object result = joinPoint.proceed();
    	long endTime = System.currentTimeMillis();
    	long executionTime = endTime - startTime;
    	
    	LOGGER.info("******************************************************************");
    	LOGGER.info("Método {} ejecutado en {} ms", joinPoint.getSignature().getName(), executionTime);
    	LOGGER.info("Entrando en el método {}", joinPoint.getSignature().getName());
    	LOGGER.info("Saliendo del método {}", joinPoint.getSignature().getName());
    	LOGGER.info("******************************************************************");

    	return result;
    	
    }
    
    @Around("execution(* com.viewnext.register.presentation.controller..*(..))")
    public Object registerLogController(ProceedingJoinPoint joinPoint) throws Throwable {
    	
    	long startTime = System.currentTimeMillis();
    	Object result = joinPoint.proceed();
    	long endTime = System.currentTimeMillis();
    	long executionTime = endTime - startTime;
    	
    	LOGGER.info("******************************************************************");
    	LOGGER.info("Método {} ejecutado en {} ms", joinPoint.getSignature().getName(), executionTime);
    	LOGGER.info("Entrando en el método {}", joinPoint.getSignature().getName());
    	LOGGER.info("Saliendo del método {}", joinPoint.getSignature().getName());
    	LOGGER.info("******************************************************************");

    	return result;
    	
    }
    
    @Around("execution(* com.viewnext.usuario.presentation.controller..*(..))")
    public Object usuarioLogController(ProceedingJoinPoint joinPoint) throws Throwable {
    	
    	long startTime = System.currentTimeMillis();
    	Object result = joinPoint.proceed();
    	long endTime = System.currentTimeMillis();
    	long executionTime = endTime - startTime;
    	
    	LOGGER.info("******************************************************************");
    	LOGGER.info("Método {} ejecutado en {} ms", joinPoint.getSignature().getName(), executionTime);
    	LOGGER.info("Entrando en el método {}", joinPoint.getSignature().getName());
    	LOGGER.info("Saliendo del método {}", joinPoint.getSignature().getName());
    	LOGGER.info("******************************************************************");

    	return result;
    	
    }
    
}

package br.com.ifsp.ifome.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerMethods() {}

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void serviceMethods() {}

    @Around("controllerMethods()")
    public Object logAroundControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String controllerClass = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        logger.info("Método {} de {} está prestes a ser executado.", methodName, controllerClass);
        logger.info("Método chamado com os argumentos: {}", Arrays.toString(args));

        Object result = joinPoint.proceed();

        logger.info("Método executado: {} do controlador: {} com retorno {}", methodName, controllerClass, result);

        return result;
    }

    @Around("serviceMethods()")
    public Object logAroundServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String ServiceClass = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        logger.info("Método {} de {} está prestes a ser executado.", methodName, ServiceClass);
        logger.info("Método chamado com os argumentos: {}", Arrays.toString(args));

        Object result = joinPoint.proceed();

        logger.info("Método executado: {} do service: {} com retorno {}", methodName, ServiceClass, result);

        return result;
    }
}

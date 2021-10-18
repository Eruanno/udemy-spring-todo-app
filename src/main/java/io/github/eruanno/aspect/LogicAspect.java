package io.github.eruanno.aspect;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
class LogicAspect {
    public static final Logger logger = LoggerFactory.getLogger(LogicAspect.class);
    private final Timer projectCreateGroupTimer;

    LogicAspect(final MeterRegistry meterRegistry) {
        this.projectCreateGroupTimer = meterRegistry.timer("logic.product.create.group");
    }

    @Pointcut("execution(* io.github.eruanno.logic.ProjectService.createGroup(..))")
    static void projectServiceCreateGroup() {
    }

    @Before("projectServiceCreateGroup()")
    void logMethodCall(JoinPoint joinPoint) {
        logger.info("Before {} with {}", joinPoint.getSignature().getName(), joinPoint.getArgs());
    }

    @Around("projectServiceCreateGroup()")
    Object aroundCreateGroup(ProceedingJoinPoint joinPoint) {
        return projectCreateGroupTimer.record(() -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
                throw new RuntimeException(e);
            }
        });
    }
}

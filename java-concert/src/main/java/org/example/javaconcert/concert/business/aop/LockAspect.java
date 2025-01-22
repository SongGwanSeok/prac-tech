package org.example.javaconcert.concert.business.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.javaconcert.concert.business.LockService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Order(1)
@Component
@RequiredArgsConstructor
public class LockAspect {

    private final LockService lockService;

    @Around("@annotation(org.example.javaconcert.concert.business.aop.NamedLock)")
    public Object handleLock(ProceedingJoinPoint joinPoint) throws Throwable {
        String lockKey = joinPoint.getSignature().getName();
        boolean isLockAcquired = lockService.getLock(lockKey);

        if (!isLockAcquired) {
            throw new IllegalStateException("Unable to acquire lock: " + lockKey);
        }

        try {
            return joinPoint.proceed();
        } finally {
            lockService.releaseLock(lockKey);
        }
    }
}

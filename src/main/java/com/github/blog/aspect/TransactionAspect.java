package com.github.blog.aspect;

import com.github.blog.util.ConnectionHolder;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.sql.Connection;

/**
 * @author Raman Haurylau
 */
@Aspect
@Log4j2
@Component
@AllArgsConstructor
public class TransactionAspect {
    private final ConnectionHolder connectionHolder;

    @Around("@annotation(com.github.blog.annotation.Transaction)")
    public Object handleTransaction(ProceedingJoinPoint pjp) throws Throwable {
        Connection conn = connectionHolder.getConnection();
        Object output = null;
        try {
            log.info("Enter transaction");
            conn.setAutoCommit(false);
            output = pjp.proceed();
            conn.commit();
            log.info("Commit transaction");
        } catch (RuntimeException e) {
            log.info("Rollback transaction");
            conn.rollback();
        } finally {
            connectionHolder.release();
        }
        return output;
    }
}

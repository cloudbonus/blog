package com.github.blog.aspects;

import com.github.blog.util.DefaultConnectionHolder;
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
    private final DefaultConnectionHolder connectionHolder;

    @Around("@annotation(com.github.blog.annotation.Transaction)")
    public Object handleTransaction(ProceedingJoinPoint pjp) throws Throwable {
        Connection con = connectionHolder.getConnection();
        Object output = null;
        try {
            log.info("Enter transaction");
            con.setAutoCommit(false);
            output = pjp.proceed();
            con.commit();
            log.info("Commit transaction");
        } catch (RuntimeException e) {
            log.info("Rollback transaction");
            con.rollback();
        } finally {
            connectionHolder.closeConnections();
        }
        return output;
    }
}

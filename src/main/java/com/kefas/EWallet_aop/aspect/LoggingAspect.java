package com.kefas.EWallet_aop.aspect;

import com.kefas.EWallet_aop.util.LocalStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.security.Principal;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    @Value(value = "AuthUser")
    private String AUTH_USER;

    @Value(value = "ActivateAuthUser")
    private String ACTIVE_AUTH_USER;

    private final LocalStorage localStorage;

    @Before(value = "serviceMethods()")
    public void loggingAdviceBefore(JoinPoint joinPoint){
        log.info("Before " + joinPoint.getSignature().getName()
                + " is called. Target object is: {}", joinPoint.getTarget());
    }

    @AfterReturning(pointcut = "paymentMethods() || walletMethods()", returning = "returnedObject")
    public void loggingAdviceAfterReturning(JoinPoint joinPoint, Object returnedObject){
        log.info("After returning " + joinPoint.getSignature().getName() +
                " is called. Target object is: {}", joinPoint.getTarget());
        log.info("ReturnedObject is {}", returnedObject);
    }

    @AfterThrowing(value = "walletMethods()")
    public void loggingAdviceAfterThrowing(JoinPoint joinPoint){
        log.info("Aftre throwing " + joinPoint.getSignature().getName() +
                " is called. Target object is: {}", joinPoint.getTarget());
    }

    @Around(value = "@annotation(com.kefas.EWallet_aop.annotation.Loggable)")
    public Object loggingAdviceAround(ProceedingJoinPoint proceedingJoinPoint){
        Object returnValue = null;

        try{
            log.info("Before around advice " + proceedingJoinPoint.getSignature().getName() + " is called");
            returnValue = proceedingJoinPoint.proceed();
            log.info("After returned around advice " + proceedingJoinPoint.getSignature().getName() +
                    " is called. Target object is: {}", proceedingJoinPoint.getTarget());
        } catch (Throwable e){
            log.error(e.getMessage());
            log.info("After throwing around advice " + proceedingJoinPoint.getSignature().getName() +
                    "is called. Target object is: {}", proceedingJoinPoint.getTarget());
        }

        log.info("After finally around advice " + proceedingJoinPoint.getSignature().getName() +
                " is called. Target object is: {}", proceedingJoinPoint.getTarget());

        return returnValue;
    }

    @Before(value = "@annotation(com.kefas.EWallet_aop.annotation.TokenLog)")
    public void loggingAdviceBeforeValidatingToken(JoinPoint joinPoint){
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        String email = "";
        if(principal != null)
            email = principal.getName();

        log.info("After returning " + joinPoint.getSignature().getName() +
                " is called. Target object is: {}", joinPoint.getTarget());

        String activeTokenKey = ACTIVE_AUTH_USER + email;
        String tokenKey = AUTH_USER + email;
        String activeToken = localStorage.getValueByKey(activeTokenKey);
        String token = localStorage.getValueByKey(tokenKey);

        boolean isTokenValid = !(activeToken == null || !activeToken.equals(token));

        log.info("ActiveAuthUser: " + activeToken);
        log.info("AuthUser: " + token);
        log.info("Is token valid? {}", isTokenValid);
    }

    @Pointcut(value = "execution(* *com.kefas.EWallet_aop.service..*(..))")
    public void serviceMethods(){}

    @Pointcut(value = "within(com.kefas.EWallet_aop.service..*))")
    public void serviceMethods2() {}

    @Pointcut(value = "within(com.kefas.EWallet_aop.service.Impl.PaymentServiceImpl))")
    public void paymentMethods() {}

    @Pointcut(value = "within(com.kefas.EWallet_aop.service.Impl.WalletServiceImpl))")
    public void walletMethods() {}
}

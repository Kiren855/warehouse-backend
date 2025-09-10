package com.sunny.scm.common.aop;

import com.sunny.scm.common.client.AuthorizationClient;
import com.sunny.scm.common.constant.GlobalErrorCode;
import com.sunny.scm.common.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class AuthorizationAspect {
    private final AuthorizationClient authClient;

    public AuthorizationAspect( AuthorizationClient authClient)
    {
        this.authClient = authClient;
    }

    @Before("@annotation(checkPermission)")
    public void checkPermission(JoinPoint joinPoint, CheckPermission checkPermission) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AppException(GlobalErrorCode.UNAUTHENTICATED);
        }

        String userId = authentication.getName();
        String permission = checkPermission.permission();

        boolean allowed = authClient.checkPermission(userId, permission);
        if (!allowed) {
            throw new AppException(GlobalErrorCode.NOT_PERMITTED);
        }
    }
}

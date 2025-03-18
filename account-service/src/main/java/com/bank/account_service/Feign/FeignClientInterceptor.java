package com.bank.account_service.Feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class FeignClientInterceptor implements RequestInterceptor {
    private static  final Logger logger= LoggerFactory.getLogger(FeignClientInterceptor.class);

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String token = getAuthTokenFromRequest();
        if (token != null) {
            requestTemplate.header("Authorization", "Bearer " + token);
            logger.info("Added Authorization header: Bearer {}", token);
        }
    }

    private String getAuthTokenFromRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            logger.warn("No request context found!");
            return null;
        }

        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            logger.warn("No Authorization header found or invalid format");
            return null;
        }

        return token.substring(7);
    }
}


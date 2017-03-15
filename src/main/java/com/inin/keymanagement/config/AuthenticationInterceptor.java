package com.inin.keymanagement.config;


import com.inin.keymanagement.config.annotations.AuthRequired;
import com.inin.keymanagement.services.HawkAuthentication;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is the authentication interceptor that looks for the AuthRequired annotation
 */
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    private HawkAuthentication authService;

    /**
     * Sets the auth service bean
     * @param authService Hawk Authorization Service
     */
    public AuthenticationInterceptor(HawkAuthentication authService){
        this.authService = authService;
    }

    /**
     * Handler before all controller requests. It checks if the auth required annotation exists, then performs operations
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param handler the HandlerMethod handler
     * @return boolean
     * @throws Exception general exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AuthRequired authRequired = handlerMethod.getMethod().getAnnotation(AuthRequired.class);
            if (authRequired != null && authRequired.required()) {
                boolean valid = authService.isAuthenticated(request);
                if (!valid) {
                    response.sendError(401, "you are not authorized to this endpoint");
                }
            }
        }
        return true;
    }

}

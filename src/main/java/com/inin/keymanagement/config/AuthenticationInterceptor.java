package com.inin.keymanagement.config;


import com.inin.keymanagement.config.annotations.AuthRequired;
import com.inin.keymanagement.models.dao.RequestLog;
import com.inin.keymanagement.services.HawkAuthentication;
import com.inin.keymanagement.services.RequestLoggingService;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * This is the authentication interceptor that looks for the AuthRequired annotation
 */
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    private HawkAuthentication authService;
    private RequestLoggingService requestLoggingService;
    private DateTimeFormatter iso8601Formatter = DateTimeFormatter.ofPattern("uuuu-MM-DD'T'HH:mm:ss.SSS");

    /**
     * Sets the auth service bean
     * @param authService Hawk Authorization Service
     */
    public AuthenticationInterceptor(HawkAuthentication authService, RequestLoggingService requestLoggingService){
        this.authService = authService;
        this.requestLoggingService = requestLoggingService;
    }

    /**
     * Handler before all controller requests. It checks if the auth required annotation exists, then performs operations
     * If authentication is required, the user's user id is pulled from the headers and logged along with the date/time,
     * endpoint, and request method.
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

                // Get the requesting user id from the header
                String userId = Optional.ofNullable(request.getHeader("purecloud-user-id")).orElse("NO-USER-ID");
                // Log the request
                requestLoggingService.addRequestLogEntry(
                        new RequestLog().withUserId(userId).withRequestUri(request.getRequestURI()).withRequestMethod(request.getMethod()));
            }
        }
        return true;
    }

}

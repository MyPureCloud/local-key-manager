package com.inin.keymanagement.controllers;


import com.inin.keymanagement.models.dao.RequestLog;
import com.inin.keymanagement.services.RequestLoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/key-management/v1")
public class HealthController {
    /**
     * Returns a simple response for a health check request.
     * @return The string "OK"
     */
    @RequestMapping(value = "/health", method = RequestMethod.GET)
    @ResponseBody
    public String healthCheck(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return "OK";
    }
}

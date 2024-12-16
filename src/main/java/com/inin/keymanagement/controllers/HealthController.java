package com.inin.keymanagement.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/key-management/v1")
public class HealthController {
    /**
     * Returns a simple response for a health check request.
     * @return The string "OK"
     */
    @GetMapping("/health")
    @ResponseBody
    public String healthCheck(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return "OK";
    }
}

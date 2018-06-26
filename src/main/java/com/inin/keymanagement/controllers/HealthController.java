package com.inin.keymanagement.controllers;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

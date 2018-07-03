package com.inin.keymanagement.controllers;


import com.inin.keymanagement.config.annotations.AuthRequired;
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
public class RequestLogController {

    @Autowired
    private RequestLoggingService requestLoggingService;

    /**
     * Gets the request log entries for the service
     * @param maxEntries Optional request parameter which will limit the number of RequestLog entries in the
     *                   response to no more than maxEntries.
     * @param
     * @return a collection of RequestLog entries, ordered from most recent to least recent
     */
    @RequestMapping(value = "/request-log", method = RequestMethod.GET)
    @AuthRequired
    @ResponseBody
    public List<RequestLog> getRequestLogs(
            @RequestParam(value = "maxEntries", required = false, defaultValue = "100")
            int maxEntries,
            @RequestParam(value = "earliestTime", required = false, defaultValue = "1970-01-01T00:00:00.000")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime earliestTime,
            @RequestParam(value = "latestTime", required = false, defaultValue = "9999-12-31T23:59:59.999")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime latestTime,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return requestLoggingService.getRequestLogEntries(maxEntries, earliestTime, latestTime);
    }

    /**
     * Deletes all the request log entries for the service
     */
    @RequestMapping(value = "/request-log", method = RequestMethod.DELETE)
    @AuthRequired
    @ResponseBody
    public void deleteRequestLogs(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        requestLoggingService.clearRequestLogEntries();
        response.setStatus(200);
    }

}

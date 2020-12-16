package com.inin.keymanagement.services;


import com.inin.keymanagement.models.dao.RequestLog;
import com.inin.keymanagement.models.repositories.RequestLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Stores, queries for, and deletes all RequestLog entries for authenticated requests
 *
 */
@Service
public class RequestLoggingService {
    private static Sort timestampSort = Sort.by(Sort.Direction.DESC, "timestamp");

    @Autowired
    RequestLogRepository requestLogRepository;

    public synchronized void addRequestLogEntry(RequestLog entry) {
        requestLogRepository.save(entry);
    }

    public synchronized List<RequestLog> getRequestLogEntries(int maxEntries, LocalDateTime earliestTime, LocalDateTime latestTime) {
        return requestLogRepository.findByTimestampBetween(earliestTime, latestTime,
                PageRequest.of(0, maxEntries, timestampSort));
    }

    public synchronized void clearRequestLogEntries() {
        requestLogRepository.deleteAll();
    }
}

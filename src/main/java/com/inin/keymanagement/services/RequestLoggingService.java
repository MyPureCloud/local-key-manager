package com.inin.keymanagement.services;


import com.inin.keymanagement.models.dao.RequestLog;
import com.inin.keymanagement.models.repositories.RequestLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Stores, queries for, and deletes all RequestLog entries for authenticated requests
 *
 */
@Service
public class RequestLoggingService {
    private static Sort timestampSort = new Sort(Sort.Direction.DESC, "timestamp");

    @Autowired
    RequestLogRepository requestLogRepository;

    public synchronized void addRequestLogEntry(RequestLog entry) {
        requestLogRepository.save(entry);
    }

    public synchronized List<RequestLog> getRequestLogEntries(int maxEntries, LocalDateTime earliestTime, LocalDateTime latestTime) {
        return requestLogRepository.findByTimestampBetween(earliestTime, latestTime,
                new PageRequest(0, maxEntries, timestampSort));
    }

    public synchronized void clearRequestLogEntries() {
        requestLogRepository.deleteAll();
    }
}

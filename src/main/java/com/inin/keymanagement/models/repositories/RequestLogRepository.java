package com.inin.keymanagement.models.repositories;

import com.inin.keymanagement.models.dao.RequestLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This is part of the hibernate magic. It is an interface that will do general look ups for request logs.
 * It also provides a timestamp-based query method.
 * Used by RequestLoggingService.
 */
public interface RequestLogRepository extends PagingAndSortingRepository<RequestLog, String> {
    List<RequestLog> findByTimestampBetween(LocalDateTime earliest, LocalDateTime latest, Pageable pageable);
}

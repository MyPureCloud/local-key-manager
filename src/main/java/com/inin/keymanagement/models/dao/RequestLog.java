package com.inin.keymanagement.models.dao;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Contains user id, request uri, and timestamp information for authenticated requests.
 */
@Entity
@Table(name = "requestlog")
public class RequestLog {
    private static final DateTimeFormatter isoFormat = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSS");

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", unique = true)
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "request_uri")
    private String requestUri;

    @Column(name = "request_method")
    private String requestMethod;

    @Column(name = "timestamp", columnDefinition = "TIMESTAMP(3)")
    private LocalDateTime timestamp;

    public RequestLog() {
        this.timestamp = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public RequestLog withId(String id) { this.id = id; return this; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public RequestLog withUserId(String userId) { this.userId = userId; return this; }

    public String getRequestUri() { return requestUri; }
    public void setRequestUri(String requestUri) { this.requestUri = requestUri; }
    public RequestLog withRequestUri(String requestUri) { this.requestUri = requestUri; return this; }

    public String getRequestMethod() { return requestMethod; }
    public void setRequestMethod(String requestMethod) { this.requestMethod = requestMethod; }
    public RequestLog withRequestMethod(String requestMethod) { this.requestMethod = requestMethod; return this; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public RequestLog withTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

    @JsonProperty("timestamp-string")
    public String getTimestampAsString() { return timestamp.format(isoFormat); }
}

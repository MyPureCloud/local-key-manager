package com.inin.keymanagement.models.dto.auth;

/**
 * Request sent to Register a new Hawk Authenticated Service
 */
public class HawkRequest {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}

package com.inin.keymanagement.models.dto;


/**
 * Response for a decrypted body
 */
public class DecryptResponse {
    private String body;

    public DecryptResponse(String body){
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

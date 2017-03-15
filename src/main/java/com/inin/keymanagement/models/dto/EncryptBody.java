package com.inin.keymanagement.models.dto;


public class EncryptBody {
    private String keypairId;
    private String body;

    public EncryptBody(){}

    public EncryptBody(String keypairId, String body){
        this.keypairId = "KeyPair id goes here";
        this.body = "encrypted base64 string is here";
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getKeypairId() {
        return keypairId;
    }

    public void setKeypairId(String keypairId) {
        this.keypairId = keypairId;
    }
}

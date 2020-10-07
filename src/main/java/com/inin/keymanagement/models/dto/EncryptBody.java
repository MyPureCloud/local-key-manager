package com.inin.keymanagement.models.dto;


public class EncryptBody {
    private String keypairId;
    private String body;
    private String decryptType;

    public EncryptBody(){}

    public EncryptBody(String keypairId, String body){
        this.keypairId = "KeyPair id goes here";
        this.body = "encrypted base64 string is here";
        this.decryptType = "decryptType goes here";
    }

    public EncryptBody(String keypairId, String body, String decryptType){
        this.keypairId = "KeyPair id goes here";
        this.body = "encrypted base64 string is here";
        this.decryptType = "decryptType goes here";
    }


    public String getDecryptType() {
        return decryptType;
    }

    public void setDecryptType(String decryptType) {
        this.decryptType = decryptType;
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

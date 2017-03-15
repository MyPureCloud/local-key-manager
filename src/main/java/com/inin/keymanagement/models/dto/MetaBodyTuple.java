package com.inin.keymanagement.models.dto;


import java.util.HashMap;

public class MetaBodyTuple {

    private HashMap<String, String> meta;
    private byte[] encryptedBody;

    public MetaBodyTuple(HashMap<String, String> meta, byte[] encryptedBody){
        this.meta = meta;
        this.encryptedBody = encryptedBody;
    }

    public HashMap<String, String> getMeta() {
        return meta;
    }

    public byte[] getEncryptedBody() {
        return encryptedBody;
    }
}

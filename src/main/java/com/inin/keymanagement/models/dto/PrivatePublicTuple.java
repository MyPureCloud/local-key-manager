package com.inin.keymanagement.models.dto;


/**
 * Tuple for a generated key pair
 */
public class PrivatePublicTuple {

    private byte[] privateKey;
    private byte[] publicKey;

    public PrivatePublicTuple(byte[] privateKey, byte[] publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public byte[] getPrivateKey() {
        return privateKey;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }
}


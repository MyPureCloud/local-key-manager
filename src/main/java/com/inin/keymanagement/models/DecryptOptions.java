package com.inin.keymanagement.models;


import com.inin.keymanagement.models.dto.DecryptResponse;
import com.inin.keymanagement.models.dto.EncryptBody;

public class DecryptOptions {
    private EncryptBody encryptBody;
    private DecryptResponse decryptResponse;

    public DecryptOptions(){
        this.encryptBody = new EncryptBody();
        this.decryptResponse = new DecryptResponse("Decrypted Response");
    }

    public DecryptResponse getDecryptResponse() {
        return decryptResponse;
    }

    public void setDecryptResponse(DecryptResponse decryptResponse) {
        this.decryptResponse = decryptResponse;
    }

    public EncryptBody getEncryptBody() {
        return encryptBody;
    }

    public void setEncryptBody(EncryptBody encryptBody) {
        this.encryptBody = encryptBody;
    }
}

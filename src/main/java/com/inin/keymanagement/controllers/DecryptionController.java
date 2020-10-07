package com.inin.keymanagement.controllers;


import com.inin.keymanagement.config.annotations.AuthRequired;
import com.inin.keymanagement.exceptions.BadRequestException;
import com.inin.keymanagement.models.DecryptOptions;
import com.inin.keymanagement.models.dto.EncryptBody;
import com.inin.keymanagement.models.dto.DecryptResponse;
import com.inin.keymanagement.services.DecryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/key-management/v1")
public class DecryptionController {

    @Autowired
    private DecryptionService decryptionService;

    /**
     * Decryption endpoint. This endpoint should return a decrypt response with a base64 decrypted body.
     * It is here to decrypt the kek of a recording.
     * Authorization should be required
     * @param encryptBody payload coming from encryption service
     * @return a decrypted body with base64 string decrypted kek
     */
    @RequestMapping(value = "/decrypt", method = RequestMethod.POST)
    @AuthRequired
    @ResponseBody
    public DecryptResponse decrypt(@RequestBody @NotNull EncryptBody encryptBody) {
        if (encryptBody != null && encryptBody.getKeypairId() != null && encryptBody.getBody() != null){
            if (encryptBody.getDecryptType() != null && !(encryptBody.getDecryptType().equals("pkcs1"))) {
                throw new BadRequestException("If provided, decryptType must be either \"pkcs1\" or null");
            }
            return decryptionService.decrypt(encryptBody.getBody(), encryptBody.getKeypairId(), encryptBody.getDecryptType());
        }
        throw new BadRequestException("Decrypt body and keypairId must be filled out");
    }

    @RequestMapping(value = "/decrypt", method = RequestMethod.OPTIONS)
    @AuthRequired
    @ResponseBody
    public DecryptOptions decryptOptions() {
        return new DecryptOptions();
    }
}

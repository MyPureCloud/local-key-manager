package com.inin.keymanagement.services;

import com.inin.keymanagement.exceptions.BadRequestException;
import com.inin.keymanagement.models.dao.Keypair;
import com.inin.keymanagement.models.dto.DecryptResponse;
import com.inin.keymanagement.models.repositories.KeypairRepository;
import com.inin.keymanagement.utils.CryptographyHelper;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.*;


/**
 * This service decrypts incoming keks for a recording
 */
@Service
public class DecryptionService {

    @Autowired
    private KeypairRepository keypairRepository;

    private static final CryptographyHelper cryptographyHelper = new CryptographyHelper();

    /**
     * Ensure the BouncyCastle provider is in java's provider list
     */
    public DecryptionService() {
        try {
            if(Security.getProvider("BC") == null) {
                Security.addProvider(new BouncyCastleProvider());
            }
        } catch (Exception var2) {}
    }

    /**
     * Decrypts the incoming kek.
     * @param base64Text of the encrypted kek
     * @param keypairId the keypairId
     * @return Decrypt response with the decrypted kek
     */
    public DecryptResponse decrypt(String base64Text, String keypairId, String decryptType){
        Keypair keypair = keypairRepository.findById(keypairId).orElse(null);
        if (keypair == null){
            throw new BadRequestException("No key pair found with the id provided");
        }

        try {
            byte[] decryptedBytes = null;
            if (decryptType != null && decryptType.equals("pkcs1")) {
                decryptedBytes = cryptographyHelper.decryptKekFromCms(Base64.decodeBase64(keypair.getPrivateKey()), base64Text);
            }
            else {
                decryptedBytes = cryptographyHelper.decryptKek(Base64.decodeBase64(keypair.getPrivateKey()), base64Text);
            }
            
            if (decryptedBytes == null) {
                throw new BadRequestException("Failed to decrypt the provided data");
            }
            
            return new DecryptResponse(Base64.encodeBase64String(decryptedBytes));
        } catch (BadRequestException e){
            throw e;
        } catch (Exception e){
            throw new BadRequestException("Failed to decrypt the provided data");
        }
    }


}

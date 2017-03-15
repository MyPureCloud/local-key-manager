package com.inin.keymanagement.utils;


import com.google.gson.Gson;
import com.inin.keymanagement.models.dto.PrivatePublicTuple;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.encodings.OAEPEncoding;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;

/**
 * This class will help with all decryption needs.
 */
public class CryptographyHelper {

    private static final Logger LOG = LoggerFactory.getLogger(CryptographyHelper.class);
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Gson gson = new Gson();
    private static final int PRIVATE_KEY_LENGTH = 3072;

    /**
     * Constructor: establishes whether the security provider already contains the bouncycastle provider
     */
    public CryptographyHelper(){
        //Add the BC provider if it hasn't been added yet
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    /**
     * This generates a keypair
     * The keypair generator uses the RSA CryptoSystem https://en.wikipedia.org/wiki/RSA_(cryptosystem)
     * Private Key Length should be set to 3072 for a good balance between security and performance
     * The private/public keys that are generated are DER Encoded. This is a strict requirement.
     * http://www.herongyang.com/Cryptography/Certificate-Format-DER-Distinguished-Encoding-Rules.html
     * @return A private public keypair tuple containing bytes.
     */
    public PrivatePublicTuple generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom secureRandom = generateNewSecureRandom();
            keyPairGenerator.initialize(PRIVATE_KEY_LENGTH, secureRandom);
            KeyPair pair = keyPairGenerator.generateKeyPair();
            PrivateKey privateKey = pair.getPrivate();
            PublicKey publicKey = pair.getPublic();
            return new PrivatePublicTuple(getDEREncodingFromPrivateKey(privateKey), getDEREncodingFromPublicKey(publicKey));
        } catch (Exception e) {
            LOG.error("There was an error generating key pair: ", e);
        }
        return null;
    }

    /**
     * This method decrypts the kek for a recording of any media type. This is the main thing that the local service will
     * be doing.
     * @param privateKey the der encoded private key bytes
     * @param encryptedKey the incoming encrypted key
     * @return the decrypted kek. This should be returned to PC.
     */
    public byte[] decryptKek(byte[] privateKey, byte[] encryptedKey){
        try {
            AsymmetricBlockCipher cipher = new OAEPEncoding(new RSAEngine(), new SHA1Digest());
            RSAPrivateKey rsaPrivateKey = RSAPrivateKey.getInstance(ASN1Sequence.fromByteArray(privateKey));
            cipher.init(false, new RSAKeyParameters(true, rsaPrivateKey.getModulus(), rsaPrivateKey.getPrivateExponent()));
            return cipher.processBlock(encryptedKey, 0, encryptedKey.length);
        } catch (Exception e) {
            LOG.error("There was an error decrypting kek: ", e);
        }
        return null;
    }

    /**
     * Helper method that allows a base64 string as the encrypted key
     * @param privateKey private key bytes
     * @param base64EncodedKey encrypted key in bytes
     * @return the decrypted kek
     */
    public byte[] decryptKek(byte[] privateKey, String base64EncodedKey) {
        return decryptKek(privateKey, Base64.decode(base64EncodedKey));
    }

    private static SecureRandom generateNewSecureRandom() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] data = new byte[1];
        secureRandom.nextBytes(data);
        return secureRandom;
    }

    private byte[] getDEREncodingFromPrivateKey(PrivateKey privateKey) {
        try {
            byte[] ex = privateKey.getEncoded();
            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(ex);
            RSAPrivateKey rsaPrivateKey = RSAPrivateKey.getInstance(privateKeyInfo.parsePrivateKey());
            return rsaPrivateKey.getEncoded("DER");
        } catch (Exception var5) {
            return null;
        }
    }

    private byte[] getDEREncodingFromPublicKey(PublicKey publicKey) {
        try {
            byte[] ex = publicKey.getEncoded();
            SubjectPublicKeyInfo subPkInfo = new SubjectPublicKeyInfo(ASN1Sequence.getInstance(ASN1Primitive.fromByteArray(ex)));
            return subPkInfo.parsePublicKey().getEncoded("DER");
        } catch (Exception var5) {
            return null;
        }
    }

}
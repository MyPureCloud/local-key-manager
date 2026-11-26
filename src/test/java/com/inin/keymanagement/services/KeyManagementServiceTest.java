package com.inin.keymanagement.services;

import com.inin.keymanagement.models.dao.Keypair;
import com.inin.keymanagement.models.repositories.KeypairRepository;
import com.inin.keymanagement.utils.CryptographyHelper;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.asn1.pkcs.RSAPublicKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KeyManagementServiceTest {

    @Mock
    private KeypairRepository keypairRepository;

    @InjectMocks
    private KeyManagementService keyManagementService;

    @Test
    public void testCreateKeyPair() throws Exception {
        when(keypairRepository.save(nullable(Keypair.class))).thenReturn(null);
        Keypair keypair = keyManagementService.createKeyPair();
        Assertions.assertNotNull(keypair);
        Assertions.assertTrue(keypair.getPublicKey().length() > 100);
        RSAPrivateKey privateDer = RSAPrivateKey.getInstance(Base64.getDecoder().decode(keypair.getPrivateKey()));
        RSAPublicKey publicDer = RSAPublicKey.getInstance(Base64.getDecoder().decode(keypair.getPublicKey()));
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = factory.generatePrivate(new RSAPrivateKeySpec(
            privateDer.getModulus(), privateDer.getPrivateExponent()));
        PublicKey publicKey = factory.generatePublic(new RSAPublicKeySpec(
            publicDer.getModulus(), publicDer.getPublicExponent()));
        Signature signature = Signature.getInstance("SHA256withRSA");
        byte[] message = "keypair round trip".getBytes(StandardCharsets.UTF_8);
        signature.initSign(privateKey);
        signature.update(message);
        byte[] signed = signature.sign();
        signature.initVerify(publicKey);
        signature.update(message);
        Assertions.assertTrue(signature.verify(signed));
    }
}

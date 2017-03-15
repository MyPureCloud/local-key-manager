package com.inin.keymanagement.services;


import com.google.common.collect.Lists;
import com.inin.keymanagement.models.dao.Keypair;
import com.inin.keymanagement.models.dto.PrivatePublicTuple;
import com.inin.keymanagement.models.repositories.KeypairRepository;
import com.inin.keymanagement.utils.CryptographyHelper;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.*;
import java.util.Date;
import java.util.List;

@Service
public class KeyManagementService {

    @Autowired
    private KeypairRepository keypairRepository;

    private static final CryptographyHelper cryptographyHelper = new CryptographyHelper();

    public Keypair createKeyPair() throws Exception {
        PrivatePublicTuple privatePublicTuple = cryptographyHelper.generateKeyPair();
        return createKeypair(privatePublicTuple.getPrivateKey(), privatePublicTuple.getPublicKey());
    }

    public Keypair getKeyPair(String id) {
        return keypairRepository.findOne(id);
    }

    public List<Keypair> getKeypairs(){
        return Lists.newArrayList(keypairRepository.findAll());
    }

    private Keypair createKeypair(byte[] privateKey, byte[] publicKey){
        Keypair keypair = new Keypair();
        keypair.setPrivateKey(Base64.encodeBase64String(privateKey));
        keypair.setPublicKey(Base64.encodeBase64String(publicKey));
        keypair.setDateCreated(new Date());
        keypairRepository.save(keypair);
        return keypair;
    }

}

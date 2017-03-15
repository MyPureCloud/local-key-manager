package com.inin.keymanagement.services;

import com.inin.keymanagement.models.dao.Keypair;
import com.inin.keymanagement.models.repositories.KeypairRepository;
import com.inin.keymanagement.utils.CryptographyHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class KeyManagementServiceTest {

    @Mock
    private KeypairRepository keypairRepository;

    @InjectMocks
    private KeyManagementService keyManagementService;

    @Test
    public void testCreateKeyPair() throws Exception {
        when(keypairRepository.save(any(Keypair.class))).thenReturn(null);
        Keypair keypair = keyManagementService.createKeyPair();
        Assert.assertNotNull(keypair);
        Assert.assertTrue(keypair.getPublicKey().length() > 100);
    }
}

package com.inin.keymanagement.services;

import com.inin.keymanagement.models.dao.Keypair;
import com.inin.keymanagement.models.repositories.KeypairRepository;
import com.inin.keymanagement.utils.CryptographyHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    }
}

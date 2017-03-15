package com.inin.keymanagement.services;

import com.inin.keymanagement.exceptions.BadRequestException;
import com.inin.keymanagement.models.dao.Keypair;
import com.inin.keymanagement.models.dto.DecryptResponse;
import com.inin.keymanagement.models.repositories.KeypairRepository;
import com.inin.keymanagement.utils.CryptographyHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;
import java.util.UUID;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DecryptionServiceTest {

    @Mock
    KeypairRepository keypairRepository;

    @InjectMocks
    DecryptionService decryptionService;


    @Test
    public void testDecryption(){
        Keypair keypair = createKeypair();
        when(keypairRepository.findOne(anyString())).thenReturn(keypair);

        DecryptResponse decryptResponse = decryptionService.decrypt(getEncryptedKey(), keypair.getId());
        Assert.assertEquals(decryptResponse.getBody(), "7+YOUOgTgqOfhKRq0//eS94VrhyCwiYbQQcgfrOaG5g=");
    }

    @Test(expected = BadRequestException.class)
    public void testNullKeyPairDecryption(){
        when(keypairRepository.findOne(anyString())).thenReturn(null);
        decryptionService.decrypt("string", "keypairId");
    }

    private Keypair createKeypair(){
        Keypair keypair = new Keypair();
        keypair.setDateCreated(new Date());
        keypair.setPrivateKey(getPrivateKey());
        keypair.setPublicKey(getPublicKey());
        keypair.setId(UUID.randomUUID().toString());
        return keypair;
    }

    private String getPrivateKey(){
        return "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";
    }

    private String getPublicKey(){
        return "MIIBigKCAYEAsUtT9x7a6T2yfDHCeSkrujBMMg8mWMR4HMxN7WBJmGnrCzcvszorzSE6VVag81doC3vPQjwG5ZY5EYHcCEnPudIPMyZ3UZ9m861oQdP+5aUjixq4heWOPjZs/ebsrT8nVRc4Dkz8n1Q/SIHW/1wZy5f/rLKXgCEKEHLcfVIugnmGOvzBzlIsTmb7R2IG44aoKB47//yuIw/H4mLh1+vxE66klrnrWH+evQ6GC7d6Y3I7AMdrRCwwaihvp4Ej5xR7ecTw6PHh1POrr1BTqM0vnK+0mQCeJeUbuQioHLyPoIZfrpotJFzUuWOKCrzG8t4grLmEsjt7wlEixKeUnpuwYfOC6JMXGSzE4A7MF/GZ0iqAY1CyEyuIPaaM8PgcTzBU2ZAYXbopdAdapSdT8WPbM1BgbwQHJmtFUAaVkGk5rpwyjMWEEH0W5x6YS4DD5KZMyOW7x0qBL6O0jO0N9AuCFPwsbaoVunY3mTJ3nhieD43Ag9HVl1rS5LwRU+m9SD8rAgMBAAE=";
    }

    private String getEncryptedKey(){
        return "ZFii5D6kHC4YdfXT2gnxYWAZ+9PFnNR7BpFkZEW+Lk0NNeQ7Zb8GQJHHwM0HnZot3PssHfOW3jOjJI/Kylel1M5hvI9r8iWnztuXgyQ/Uri6CDOG0dvtOcosWyfHshRPQIvEG4K/IYqowKyklpRLBpgUoPaEGxGCF8BRNhSzCykcztABxFHViRmHcDqTwuudZ4pleQmvfPwjf7Qf91W7v5B8QsLy2lqliBZlHgYNZbKBL8G4KZAsTASd+XNzjtYyW4JfyUVDzF1Rh1rJey6sMSbDgP7mNg9WzTo/jwPNTyuLg8bWuFpcjyqs30Ou+ArruzOBsKHuOwFwJ3xvzkbSTckjhpoGR1E4MlBufM42yUfvX6pVJsGISUsVw6da7Cg6OV+YG6S+eBxlvjMx7aPN0S1o3R5riM7niDyVByxXEEVqpy1oxW9q+G7z4cOj4N6VS/Z9/+lJAsKjHdD9Ym1R1xSkaO+rzCRTzlzMrWGf7SRc1ETZ+MMUDc7+aAiy6z3o";
    }
}

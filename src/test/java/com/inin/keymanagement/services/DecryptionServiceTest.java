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

        DecryptResponse decryptResponse = decryptionService.decrypt(getEncryptedKey(), keypair.getId(), null);
        Assert.assertEquals(decryptResponse.getBody(), "7+YOUOgTgqOfhKRq0//eS94VrhyCwiYbQQcgfrOaG5g=");
    }

    @Test
    public void testDecryptionOfPKCS1() {
        Keypair keypair = createCMSKeypair();
        when(keypairRepository.findOne(anyString())).thenReturn(keypair);

        DecryptResponse decryptResponse = decryptionService.decrypt(getPKCS1EncryptedDek(), keypair.getId(), "pkcs1");
        Assert.assertEquals("PKIKLwo2+u9VH4tDHRizx8Lk9eyz2H/yUA6P6uqsKy8=", decryptResponse.getBody());
    }

    @Test(expected = BadRequestException.class)
    public void testNullKeyPairDecryption(){
        when(keypairRepository.findOne(anyString())).thenReturn(null);
        decryptionService.decrypt("string", "keypairId", null);
    }

    private Keypair createKeypair(){
        Keypair keypair = new Keypair();
        keypair.setDateCreated(new Date());
        keypair.setPrivateKey(getPrivateKey());
        keypair.setPublicKey(getPublicKey());
        keypair.setId(UUID.randomUUID().toString());
        return keypair;
    }

    private Keypair createCMSKeypair() {
        Keypair keypair = new Keypair();
        keypair.setDateCreated(new Date());
        keypair.setPrivateKey(getPrivateKeyForPKCS1Dek());
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

    private String getPrivateKeyForPKCS1Dek() {
        return "MIIG4wIBAAKCAYEAktJdZ9uGj8pPBWHb3Sr+qCc6YO7bN6fjbXWnk1TVIZl76elcLro5a2hJtJgb7mH5aQVklfxaF37MnIPzn8RYJnFfyOtVjLoj42A9+loQlTj8slZmT3N2GG7QeG+etlxreRqwmp3UaNNd/68cEysnw3wS+LkhZVaF7e33ObL4PyKDcCOVKGxVcvIZU1gDdEdz+miFs0sfPtAVkFKRigYHh2UET1JyZ+XVfXabOODlJh6uKPQrLedLdkv01/UV8ENFPJwPuSLOgX0LiomFMDlTe/tUtl/X/NkTCgc332f2ZEQY2xRzCprygCPb6Q13mnBj1aakFnMvl0o1wrYBtSNgOxS+8EJJXE6bo43L00yWvL3Y86bS4L6aqP3hkgsB1TizwzUvM8KAg3B3cgTUAsqOxk9jgss2dXSUKj0KgxtN0XVGCBn83T4UEbDdRTzl6pT6f9busoaMkEI4El/FghNyU+/TEk3yK8PM4VlXGiwHHPh3l9FQLSeZ6t/H9DZATRWXAgMBAAECggGAPwF/i/4s9K/A0XJ5Q6QbvFV5O/fEo1Gsy2BeFV+mRtDy2CKIqJTJ50nNt24/LkFlIqhpzeOJVePYMqJ9feZoXbbxf+tdvxJyLDU046aazy6xYmapaRcHatzDIavk1CU9Ca9mcjOg34s1H3AqAB/Y5I7uJUZtCmvi9YPVQDGJTq6k8Q6UB9DVqooyTA3NDa1MG/2WwqZONkpdtp14T17fBD9ZZiDfb661uy2W1wXL0FwCKRj/HqfPPmjCHZrTW1PCTjhWvlQlfejJmTJ/1ZnZVte3C2AaLJrOE9+v/bmtV56qSctVLfVvfDpEEMeXcSC1IwxlaOq3zwYRcVgE8tvBtbbfT9d2Sw//Sxjh8k6t2SztQZa93HK9qpn6mvfNXAB/R2PdN3ZVog4zNCu/iAWzKQ8vXQQmfWHJ8f8mhzPe1F8zMm1Sz0uMD17NKJEeBPMIoA2XvYbFxwRzq5m4YUnsFGoiPQgnXEJ+iZm99lrdhblQAQG2xAZAkFJUBVdDgj7BAoHBAOHAXuU59Sf149LHmTDlH2U9Ab9IdkSRQ+xT5qXE5uxwA9PwGi5wB28e61i+jXAWIJ2MNwJW+0jWZMfq8NJs/G0yyRUfDDQ+KVSpzCUMOUOgC59SxWaIFb1xV/d1vpTM3wocSAxmTOQ7zpcTqTnIhts/IupAAn0ew1IpIhcoa0npjmxrvFZb43IIhhRpTITZQj3S+URGVwNj3prstuq0RDN+wXEevXE4jFjCgL35Dswmvzyq6r541retpdvJKkpyWQKBwQCmfpTEhKZbU+TjFbE6/WlHH3xV3/OJRBtxnCAO/EYbo0ZurYN8Dq/r1BxpBljh28oVroCLyg5si+pmOfNUq39/vbkQDYTckKlnrzHJ2DsHyFyUoPS50OpiET3crpno383OXl44n2emBBOykpzS78v/k9W6613mkIPf+ANOsIbrU94wKBsOID0fSR+GeqAmn/X7QBLk/3psCu1v5l5EC1n6z0+I1zLw2d9j6+6ScVd8jvbsjn64u1GTqBAJ+lA3aW8CgcB/atKk0seaXGDQUTQXstq7i2l3C3ElVlBeDST7n10vSjH+51//od+wSmVPW439G6AhDEjoGywNemS7qdFvE3COOcHhF/sNaaBuVsAQkQupLaYhsNgV/O04VcbutxhwaAnjbP8FcKApmdAK2WAEazgie0o7qRE+iaR4tRq2s2uK9jYa39wXahm//SugTaaQvQd0WaWzcXgfQ7ka/ExpwIFWB08rh8wpIV+loXKExxQhCiWaGmYQR6f5WBtrJ+mkx1ECgcAdIQRSJYsda14eRMtPMMrcgKu2MopfN6U9iT0qogR6DWTjNEROzhsFPYUC8vNWAKgohvFCg8VmRExsOjNSWvbhmIXqQxM9q6mX1FT3pXcec3WPI2HtqJl5Mt8J3xpaXd2Z/3Son2r45rmzgmchmIauXmUEYO16y0nnQ3MAr23Lek7jSgEBxa4r7RUw5/9hApd8r8cefXwjrv2/mCRh70J82QxnOo9VCClHSB3jTSUrS6/zzzJoi41IMh0sQbkLGM0CgcEAoL7gTrFJPdiWsUuOLlUq8tNbMPk60UlIxl6NE1mU7Pcxkga9QtRYWYiXxY4Tc9m1Lbd5m47px3YsUdwC99fjH5+vnqg/fOr+Jyco7zOTduBMQS0O0xx7z3mHxenpaHU1Ac1eOh2nZ2abs3G/IyzM1v2TKdJPwk3we27/eqv8n/fFLHZ+kGt1dpYg/ZfKLGoYoLpHVmnJ/MBFwdhHN7Bc7lmjSVEP2xdj9Tj+xC8xmCvLklkQ2qbw5o0PUkPDIsCI";
    }

    private String getPKCS1EncryptedDek() {
        return "S5exH1pFWh3y97wboy3ctDkaZhxVeNrUcD1YVrtbsLmA+9ioojHt1QfqmeLilAtN+/kAlfFfdwRq5kodhNKAqRrihI/0nnbKn4OVfCIM4zDRnX13FrhdEvKQwr6jVCstr0qZ/0lKXjvyDnvXiUIAXSIpzgRBEqK+3ZcSjQs9J3Em5QtG5wow/bwBNhcucHBdkIUMDS/uE56F1ZKja5WKrMzfBpnjtLFT2dXa6I3DBNCgzIeRrNuYZfYbt5POrgzB6uJkuyOdQiTjUXQM8Xe1oSC/n5vOdU7CGXrc7dW/CSjQhiCk92L2x02GKWNcF/EDJeB0+LoO6ktH2YoIKQpPaol2+PcoIoe2/Lp6DPqbxTSoze35Tqml7fmzeAR9rC9DSSQHoDxIw+ppBguU/YwL16him9O7hzx+Vjk9llIYSNoIErWnVFC9FKJ62gJ6Jpz4oX37UwdH01Mv+Dn0wlSmsPDyAYNf0V1n/OfNkh9ahXvHb0woGzsOrfyCYnDWOQUC";
    }
}

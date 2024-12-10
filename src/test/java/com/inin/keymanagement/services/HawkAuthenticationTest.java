package com.inin.keymanagement.services;

import com.inin.keymanagement.exceptions.BadRequestException;
import com.inin.keymanagement.models.dao.HawkModel;
import com.inin.keymanagement.models.dto.auth.HawkRequest;
import com.inin.keymanagement.models.repositories.HawkDaoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.WARN)
@ExtendWith(MockitoExtension.class)
public class HawkAuthenticationTest {

    @Mock
    HawkDaoRepository repository;

    @InjectMocks
    HawkAuthentication hawkAuthentication;

    @Test
    public void testHawkRegister(){
        HawkModel hModel = new HawkModel();
        hModel.setAlgorithm("sha256");
        hModel.setId("DerekM");
        hModel.setAuthKey("SomeKey");
        when(repository.findById(nullable(String.class))).thenReturn(Optional.empty());
        when(repository.save(nullable(HawkModel.class))).thenReturn(new HawkModel());
        HawkRequest hr = new HawkRequest();
        hr.setId("DerekM");
        HawkModel hm = hawkAuthentication.registerService(hr);
        Assertions.assertEquals(hModel.getId(), "DerekM");
        Assertions.assertEquals(hModel.getAuthKey(), "SomeKey");
        Assertions.assertEquals(hModel.getAlgorithm(), "sha256");
    }

    @Test
    public void hawkNoDuplicateIds(){
        assertThrows(BadRequestException.class, () -> {
            when(repository.findById(nullable(String.class))).thenReturn(java.util.Optional.of(new HawkModel()));
            HawkRequest hr = new HawkRequest();
            hr.setId("Derek");
            hawkAuthentication.registerService(hr);
        });
    }

}

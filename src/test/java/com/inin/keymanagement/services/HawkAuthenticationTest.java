package com.inin.keymanagement.services;

import com.inin.keymanagement.exceptions.BadRequestException;
import com.inin.keymanagement.models.dao.HawkModel;
import com.inin.keymanagement.models.dto.auth.HawkRequest;
import com.inin.keymanagement.models.repositories.HawkDaoRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
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
        when(repository.findOne(anyString())).thenReturn(null);
        when(repository.save(any(HawkModel.class))).thenReturn(new HawkModel());
        HawkRequest hr = new HawkRequest();
        hr.setId("DerekM");
        HawkModel hm = hawkAuthentication.registerService(hr);
        Assert.assertEquals(hModel.getId(), "DerekM");
        Assert.assertEquals(hModel.getAuthKey(), "SomeKey");
        Assert.assertEquals(hModel.getAlgorithm(), "sha256");
    }

    @Test(expected = BadRequestException.class)
    public void hawkNoDuplicateIds(){
        when(repository.findOne(anyString())).thenReturn(new HawkModel());
        HawkRequest hr = new HawkRequest();
        hr.setId("Derek");
        hawkAuthentication.registerService(hr);
    }

}

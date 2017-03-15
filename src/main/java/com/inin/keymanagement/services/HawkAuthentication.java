package com.inin.keymanagement.services;


import com.inin.keymanagement.exceptions.BadRequestException;
import com.inin.keymanagement.models.dao.HawkModel;
import com.inin.keymanagement.models.dto.auth.HawkRequest;
import com.inin.keymanagement.models.repositories.HawkDaoRepository;
import net.jalg.hawkj.Algorithm;
import net.jalg.hawkj.AuthorizationHeader;
import net.jalg.hawkj.HawkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.net.URI;
import java.security.SecureRandom;

/**
 * This class handles the hawk authentication. https://github.com/hueniverse/hawk
 */
@Service
public class HawkAuthentication {

    @Autowired
    private HawkDaoRepository repository;

    /**
     * Method used to register the service needing hawk authentication
     * @param hr HawkRequest
     * @return the hawkmodel with important service related information
     */
    public HawkModel registerService(HawkRequest hr) {
        if (hr.getId() == null || hr.getId().length() < 6 || repository.findOne(hr.getId()) != null) {
            throw new BadRequestException("authentication needs a unique id and must be over 5 characters");
        }
        HawkModel hm = generateHawkModel(hr.getId());
        return repository.save(hm);
    }

    /**
     * Retrieves a hawk model
     * @param id hawk model id
     * @return hawk model
     */
    public HawkModel getHawkModel(String id) {
        return repository.findOne(id);
    }

    /**
     * Used mostly by the auth interceptor. Determines if the request is valid
     * @param request the incoming servlet request
     * @return a boolean whether the user is authenticated
     * @throws Exception throws general exception
     */
    public boolean isAuthenticated(HttpServletRequest request) throws Exception {
        AuthorizationHeader authorizationHeader = AuthorizationHeader.authorization(request.getHeader(HttpHeaders.AUTHORIZATION));
        if (authorizationHeader == null || authorizationHeader.getId() == null) {
            return false;
        }
        HawkModel hm = getHawkModel(authorizationHeader.getId());
        if (hm == null){
            return false;
        }

        HawkContext hawk = HawkContext.request(request.getMethod(), request.getServletPath(), request.getServerName(), request.getServerPort())
                .credentials(hm.getId(), hm.getAuthKey(), Algorithm.fromString(hm.getAlgorithm()))
                .tsAndNonce(authorizationHeader.getTs(), authorizationHeader.getNonce())
                .build();

        return hawk.isValidMac(authorizationHeader.getMac());
    }

    private HawkModel generateHawkModel(String id){
        HawkModel hm = new HawkModel();
        hm.setId(id);
        hm.setAuthKey(new BigInteger(130, new SecureRandom()).toString());
        hm.setAlgorithm("sha256");
        return hm;
    }
}

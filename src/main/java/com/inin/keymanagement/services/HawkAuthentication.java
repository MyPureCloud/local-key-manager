package com.inin.keymanagement.services;


import com.inin.keymanagement.exceptions.BadRequestException;
import com.inin.keymanagement.models.dao.HawkModel;
import com.inin.keymanagement.models.dto.auth.HawkRequest;
import com.inin.keymanagement.models.repositories.HawkDaoRepository;
import net.jalg.hawkj.Algorithm;
import net.jalg.hawkj.AuthorizationHeader;
import net.jalg.hawkj.HawkContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * This class handles the hawk authentication. https://github.com/hueniverse/hawk
 */
@Service
public class HawkAuthentication {
    private static final Logger LOG = LoggerFactory.getLogger(HawkAuthentication.class);

    @Autowired
    private HawkDaoRepository repository;

    /**
     * Method used to register the service needing hawk authentication
     * @param hr HawkRequest
     * @return the hawkmodel with important service related information
     */
    public HawkModel registerService(HawkRequest hr) {
        if (hr.getId() == null || hr.getId().length() < 6 || repository.findById(hr.getId()).isPresent()) {
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
        return repository.findById(id).orElse(null);
    }

    /**
     * Used mostly by the auth interceptor. Determines if the request is valid
     * @param request the incoming servlet request
     * @return a boolean whether the user is authenticated
     * @throws Exception throws general exception
     */
    public boolean isAuthenticated(HttpServletRequest request) throws Exception {
        if (null == request.getHeader(HttpHeaders.AUTHORIZATION)) {
            LOG.warn("No authorization header present");
            return false;
        }
        LOG.debug("Raw Authorization header <{}>", request.getHeader(HttpHeaders.AUTHORIZATION));
        AuthorizationHeader authorizationHeader = AuthorizationHeader.authorization(request.getHeader(HttpHeaders.AUTHORIZATION));
        if (authorizationHeader == null || authorizationHeader.getId() == null) {
            LOG.warn("No authorization header: Raw Authorization header <{}>",
                    request.getHeader(HttpHeaders.AUTHORIZATION));
            return false;
        }
        LOG.debug("Parsed Authorization header <{}>", authorizationHeader);
        HawkModel hm = getHawkModel(authorizationHeader.getId());
        if (hm == null){
            LOG.error("HawkModel is null");
            return false;
        }
        LOG.debug("Hawk model <{}>", hm);

        HawkContext hawk = HawkContext.request(request.getMethod(), request.getServletPath(), request.getServerName(), request.getServerPort())
                .credentials(hm.getId(), hm.getAuthKey(), Algorithm.fromString(hm.getAlgorithm()))
                .tsAndNonce(authorizationHeader.getTs(), authorizationHeader.getNonce())
                .build();
        try {
            boolean t = hawk.isValidMac(authorizationHeader.getMac());
        } catch (Exception e) {
            LOG.error("Exception during haw.isValidMac(): ", e);
        }

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

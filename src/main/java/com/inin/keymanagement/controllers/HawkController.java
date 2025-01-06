package com.inin.keymanagement.controllers;

import com.inin.keymanagement.models.dao.HawkModel;
import com.inin.keymanagement.models.dto.auth.HawkRequest;
import com.inin.keymanagement.services.HawkAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/key-management/v1/auth")
public class HawkController {

    @Autowired
    private HawkAuthentication hawkAuthentication;

    /**
     * This endpoint is here to register the service using the local key service.
     * For ININ products, the information returned should be sent to the local configuration endpoint, so that we can
     * register the authentication.
     * @param hr HawkRequest Body
     * @return HawkModel with authentication information
     */
    @PostMapping("")
    @ResponseBody
    public HawkModel registerService(@RequestBody HawkRequest hr) {
        return hawkAuthentication.registerService(hr);
    }

}

package com.inin.keymanagement.controllers;

import com.inin.keymanagement.config.annotations.AuthRequired;
import com.inin.keymanagement.models.dao.Keypair;
import com.inin.keymanagement.services.KeyManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/key-management/v1/keypair")
public class KeypairController {

    @Autowired
    private KeyManagementService keyManagementService;

    /**
     * Creates a Der Encoded key pair
     * @return KeyPair
     * @throws Exception general exception
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @AuthRequired
    @ResponseBody
    public Keypair createKeypair() throws Exception {
        return keyManagementService.createKeyPair();
    }

    /**
     * Retrieves a keypair id
     * @param keypairId the keypair id
     * @return KeyPair
     * @throws Exception throws general exception
     */
    @RequestMapping(value = "/{keypairId}", method = RequestMethod.GET)
    @AuthRequired
    @ResponseBody
    public Keypair getKeypair(@PathVariable("keypairId") String keypairId) throws Exception {
        return keyManagementService.getKeyPair(keypairId);
    }

    /**
     * Retrieves a list of keypairs
     * @return List of Keypairs
     * @throws Exception throws general exception
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @AuthRequired
    @ResponseBody
    public List<Keypair> getKeypairs() throws Exception{
        return keyManagementService.getKeypairs();
    }
}

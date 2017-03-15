package com.inin.keymanagement.models.repositories;


import com.inin.keymanagement.models.dao.Keypair;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * This is the KeyPair repository made by hibernate. It is used by Key Management Service and Decryption Service.
 */
public interface KeypairRepository extends CrudRepository<Keypair, String> {
}

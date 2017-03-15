package com.inin.keymanagement.models.repositories;


import com.inin.keymanagement.models.dao.HawkModel;
import org.springframework.data.repository.CrudRepository;

/**
 * This is part of the hibernate magic. It is an interface that will do general look ups for hawk related information.
 * This is used by the HawkAuthentication service
 */
public interface HawkDaoRepository extends CrudRepository<HawkModel, String> {
}

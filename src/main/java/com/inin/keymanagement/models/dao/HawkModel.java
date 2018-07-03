package com.inin.keymanagement.models.dao;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * Hibernate Hawk Model. Hibernate is an orm. The data here will map to the database configured.
 */
@Entity
@Table(name = "auth")
public class HawkModel {

    @Id
    @Column(name = "id", unique = true)
    private String id;
    @Column(name = "auth_key")
    private String authKey;
    @Column(name="algorithm")
    private String algorithm;

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("authKey", authKey)
                .append("algorithm", algorithm)
                .toString();
    }
}

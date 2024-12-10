package com.inin.keymanagement.models.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.*;
import java.util.Date;

/**
 * Hibernate keypair model. Hibernate is an orm. This will store the data for keypairs in the configured databased
 */
@Entity
@Table(name = "keypair")
public class Keypair {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", unique = true)
    private String id;

    @Column(name = "private_key", length = 100000)
    private String privateKey;

    @Column(name = "public_key", length = 100000)
    private String publicKey;

    @Column(name = "date_created")
    private Date dateCreated;

    @JsonIgnore
    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}

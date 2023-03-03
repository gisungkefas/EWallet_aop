package com.kefas.EWallet_aop.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@AllArgsConstructor
@MappedSuperclass
public abstract class Base implements Serializable {

    private static final long serialVersionUserID = -3290965513559831382L;

    @Id
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    @PrePersist
    private void setCreatedAt(){
        createdDate = new Date();
    }

    @PreUpdate
    private void setUpdatedAt(){
        updatedDate = new Date();
    }

    Base(){
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }

 }

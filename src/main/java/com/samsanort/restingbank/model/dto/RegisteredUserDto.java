package com.samsanort.restingbank.model.dto;

import java.io.Serializable;

/**
 * DTO info holder for a registered user.
 */
public class RegisteredUserDto implements Serializable {

    private Long id;
    private Long accountId;

    public RegisteredUserDto(Long id, Long accountId) {
        this.id = id;
        this.accountId = accountId;
    }

    public RegisteredUserDto() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}

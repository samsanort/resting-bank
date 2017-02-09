package com.samsanort.restingbank.model.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TODO add description
 */
public class UserDto {

    private String email;
    private String password;
    private Long id; // TODO remove!!

    // TODO remove
    public UserDto(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public UserDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserDto(){}

    public String getEmail() { return email; }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // TODO remove!!
    public Long getId(){ return this.id; }
    public void setId(long id){ this.id = id; }
}

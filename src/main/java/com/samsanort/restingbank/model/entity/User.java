package com.samsanort.restingbank.model.entity;

import javax.persistence.*;

/**
 * User entity.
 */
@Entity
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String email;
    private String password;

    @OneToOne
    private BankAccount account;

    public User(){}

    /**
     *
     * @param email
     * @param password
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     *
     * @param id
     * @param email
     * @param password
     */
    public User(Long id, String email, String password) {
        this(email, password);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) { this.id = id; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BankAccount getAccount() {
        return account;
    }

    public void setAccount(BankAccount account) {
        this.account = account;
    }
}

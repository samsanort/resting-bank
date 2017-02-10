package com.samsanort.restingbank.repository;

import com.samsanort.restingbank.model.entity.BankAccount;
import com.samsanort.restingbank.model.entity.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository interface for bank account persistence operations.
 */
public interface BankAccountRepository extends CrudRepository<BankAccount, Long>{
}

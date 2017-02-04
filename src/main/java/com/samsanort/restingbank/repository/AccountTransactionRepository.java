package com.samsanort.restingbank.repository;

import com.samsanort.restingbank.model.entity.AccountTransaction;
import com.samsanort.restingbank.model.entity.User;
import org.springframework.data.repository.CrudRepository;

/**
 * TODO add description
 */
public interface AccountTransactionRepository extends CrudRepository<AccountTransaction, Long>{
}

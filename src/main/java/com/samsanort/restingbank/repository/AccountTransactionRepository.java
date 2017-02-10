package com.samsanort.restingbank.repository;

import com.samsanort.restingbank.model.entity.AccountTransaction;
import com.samsanort.restingbank.model.entity.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository interface for account transaction persistence operations.
 */
public interface AccountTransactionRepository extends CrudRepository<AccountTransaction, Long>{
}

package com.samsanort.restingbank.repository;

import com.samsanort.restingbank.model.entity.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository interface for user persistence operations.
 */
public interface UserRepository extends CrudRepository<User, Long>{

    /**
     * Find a user given its email.
     * @param email user email.
     * @return The user identified by the provided email.
     */
    User findByEmail(String email);
}

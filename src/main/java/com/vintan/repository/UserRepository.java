package com.vintan.repository;

import com.vintan.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing User entities.
 * Provides standard CRUD operations and query methods based on the user's ID.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // Additional custom query methods can be added here if needed
}

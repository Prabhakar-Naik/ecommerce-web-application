package com.springboot.ecommerce.users.repository;

import com.springboot.ecommerce.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author prabhakar, @Date 10-09-2024
 */
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    User findByEmail(String username);
}

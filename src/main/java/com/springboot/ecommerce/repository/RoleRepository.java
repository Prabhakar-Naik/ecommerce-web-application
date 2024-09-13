package com.springboot.ecommerce.repository;

import com.springboot.ecommerce.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author prabhakar, @Date 11-09-2024
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String role);
}

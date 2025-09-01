package com.sunny.scm.identity.repository;

import com.sunny.scm.identity.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByIdAndIsActiveTrue(Long id);
}

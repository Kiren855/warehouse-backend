package com.sunny.scm.identity.repository;

import com.sunny.scm.identity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

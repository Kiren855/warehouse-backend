package com.sunny.scm.identity.repository;

import com.sunny.scm.identity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    Optional<User> findByEmail(String email);
    @Query("SELECT DISTINCT r.roleName FROM User u " +
            "JOIN u.groups g " +
            "JOIN g.roles r " +
            "WHERE u.userId = :userId")
    List<String> findRolesByUserId(@Param("userId") String userId);
}

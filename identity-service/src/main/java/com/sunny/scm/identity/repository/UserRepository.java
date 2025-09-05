package com.sunny.scm.identity.repository;

import com.sunny.scm.identity.constant.UserType;
import com.sunny.scm.identity.entity.Role;
import com.sunny.scm.identity.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    Optional<User> findByUserId(String userId);
    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    @Query("SELECT DISTINCT r FROM User u " +
            "JOIN u.groups g " +
            "JOIN g.roles r " +
            "WHERE u.userId = :userId")
    List<Role> findRolesByUserId(@Param("userId") String userId);

    Page<User> findAllByCompanyIdAndUserType(Long companyId, UserType userType, Pageable pageable);
}

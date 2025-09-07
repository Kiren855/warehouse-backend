package com.sunny.scm.identity.repository;

import com.sunny.scm.identity.entity.Group;
import com.sunny.scm.identity.entity.Role;
import com.sunny.scm.identity.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsByCompanyIdAndGroupName(Long companyId, String name);

    Page<Group> findAllByCompanyId(Long companyId, Pageable pageable);

    @Query("SELECT r FROM Group g JOIN g.roles r WHERE g.id = :groupId")
    Page<Role> findRolesByGroupId(@Param("groupId") Long groupId, Pageable pageable);

    @Query("SELECT u FROM Group g JOIN g.users u WHERE g.id = :groupId")
    Page<User> findUsersByGroupId(@Param("groupId") Long groupId, Pageable pageable);
}

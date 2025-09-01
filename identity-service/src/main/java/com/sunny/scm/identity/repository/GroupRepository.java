package com.sunny.scm.identity.repository;

import com.sunny.scm.identity.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsByCompanyIdAndGroupName(Long companyId, String name);
}

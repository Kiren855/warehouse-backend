package com.sunny.scm.identity.repository;

import com.sunny.scm.identity.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsByCompanyIdAndGroupName(Long companyId, String name);

    Page<Group> findAllByCompanyId(Long companyId, Pageable pageable);
}

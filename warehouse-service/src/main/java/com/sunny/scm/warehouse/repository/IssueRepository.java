package com.sunny.scm.warehouse.repository;

import com.sunny.scm.warehouse.entity.GoodIssue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<GoodIssue, Long> {
}

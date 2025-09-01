package com.sunny.scm.identity.repository;

import com.sunny.scm.identity.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByTaxId(String taxId);
    Optional<Company> findByName(String name);
}

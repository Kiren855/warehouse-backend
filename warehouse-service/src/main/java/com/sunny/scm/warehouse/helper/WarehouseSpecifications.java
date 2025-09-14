package com.sunny.scm.warehouse.helper;

import com.sunny.scm.warehouse.entity.Warehouse;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class WarehouseSpecifications {
    public static Specification<Warehouse> belongsToCompany(Long companyId) {
        return SpecificationUtils.equal("companyId", companyId);
    }

    public static Specification<Warehouse> likeCodeOrName(String keyword) {
        return SpecificationUtils.orLikeIgnoreCase(List.of("warehouseCode", "warehouseName"), keyword);
    }

    // Filter theo creation date
    public static Specification<Warehouse> createdBetween(LocalDate from, LocalDate to) {
        return SpecificationUtils.betweenDate("creationTimestamp", from, to);
    }
}

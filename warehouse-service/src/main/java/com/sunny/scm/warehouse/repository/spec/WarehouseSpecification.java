package com.sunny.scm.warehouse.repository.spec;

import com.sunny.scm.warehouse.entity.Warehouse;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class WarehouseSpecification {
    public static Specification<Warehouse> filter(
            Long companyId,
            String keyword,
            LocalDate createdFrom,
            LocalDate createdTo
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // filter theo công ty
            predicates.add(cb.equal(root.get("companyId"), companyId));

            // filter theo keyword
            if (keyword != null && !keyword.isBlank()) {
                String likePattern = "%" + keyword.toLowerCase() + "%";
                Predicate codePredicate = cb.like(cb.lower(root.get("warehouseCode")), likePattern);
                Predicate namePredicate = cb.like(cb.lower(root.get("warehouseName")), likePattern);
                predicates.add(cb.or(codePredicate, namePredicate));
            }

            // filter theo ngày tạo
            if (createdFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("creationTimestamp"), createdFrom.atStartOfDay()));
            }
            if (createdTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("creationTimestamp"), createdTo.atTime(LocalTime.MAX)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

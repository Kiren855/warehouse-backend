package com.sunny.scm.product.helper;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SpecificationUtils {
    public static <T> Specification<T> likeIgnoreCase(String field, String value) {
        return (root, query, cb) ->
                (value == null || value.isBlank())
                        ? cb.conjunction()
                        : cb.like(cb.lower(root.get(field).as(String.class)), "%" + value.toLowerCase() + "%");
    }

    public static <T> Specification<T> betweenDate(String field, LocalDate from, LocalDate to) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (from != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get(field).as(java.time.LocalDateTime.class), from.atStartOfDay()));
            }
            if (to != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get(field).as(java.time.LocalDateTime.class), to.atTime(LocalTime.MAX)));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static <T> Specification<T> orLikeIgnoreCase(List<String> fields, String value) {
        return (root, query, cb) -> {
            if (value == null || value.isBlank()) return cb.conjunction();
            String likePattern = "%" + value.toLowerCase() + "%";
            List<Predicate> predicates = fields.stream()
                    .map(f -> cb.like(cb.lower(root.get(f).as(String.class)), likePattern))
                    .toList();
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }

    public static <T> Specification<T> equal(String field, Object value) {
        return (root, query, cb) ->
                value == null ? cb.conjunction() : cb.equal(root.get(field), value);
    }
}

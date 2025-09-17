package com.sunny.scm.warehouse.helper;

import com.sunny.scm.warehouse.constant.BinStatus;
import com.sunny.scm.warehouse.constant.BinType;
import com.sunny.scm.warehouse.constant.ReceiptStatus;
import com.sunny.scm.warehouse.constant.SourceType;
import com.sunny.scm.warehouse.entity.Bin;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class ReceiptSpecifications {
    public static Specification<Bin> belongsToCompany(Long companyId) {
        return (root, query, cb) -> cb.equal(root.get("companyId"), companyId);
    }

    public static Specification<Bin> likeReceiptNumber(String keyword) {
        return SpecificationUtils.orLikeIgnoreCase(List.of("receiptNumber"), keyword);
    }

    public static Specification<Bin> updatedBetween(LocalDate from, LocalDate to) {
        return SpecificationUtils.betweenDate("updateTimestamp", from, to);
    }

    public static Specification<Bin> hasSourceType(SourceType sourceType) {
        return (root, query, cb) ->
                sourceType == null ? cb.conjunction() : cb.equal(root.get("sourceType"), sourceType);
    }

    public static Specification<Bin> hasStatus(ReceiptStatus status) {
        return (root, query, cb) ->
                status == null ? cb.conjunction() : cb.equal(root.get("receiptStatus"), status);
    }

}

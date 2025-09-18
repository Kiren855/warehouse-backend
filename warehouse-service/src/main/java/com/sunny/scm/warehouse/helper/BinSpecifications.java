package com.sunny.scm.warehouse.helper;

import com.sunny.scm.warehouse.constant.BinStatus;
import com.sunny.scm.warehouse.constant.BinType;
import com.sunny.scm.warehouse.constant.ZoneType;
import com.sunny.scm.warehouse.entity.Bin;
import com.sunny.scm.warehouse.entity.Zone;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import javax.swing.plaf.SeparatorUI;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BinSpecifications {
    public static Specification<Bin> belongsToZone(Long zoneId) {
        return (root, query, cb) -> cb.equal(root.get("zone").get("id"), zoneId);
    }

    public static Specification<Bin> likeCodeOrName(String keyword) {
        return SpecificationUtils.orLikeIgnoreCase(List.of("binCode", "binName"), keyword);
    }

    public static Specification<Bin> updatedBetween(LocalDate from, LocalDate to) {
        return SpecificationUtils.betweenDate("updateTimestamp", from, to);
    }

    public static Specification<Bin> hasBinType(BinType binType) {
        return (root, query, cb) ->
                binType == null ? cb.conjunction() : cb.equal(root.get("binType"), binType);
    }

    public static Specification<Bin> hasStatus(BinStatus status) {
        return (root, query, cb) ->
                status == null ? cb.conjunction() : cb.equal(root.get("binStatus"), status);
    }

    public static Specification<Bin> hasContentStatus(String contentStatus) {
        return (root, query, cb) -> {
            if (contentStatus == null || contentStatus.isBlank()) {
                return cb.conjunction();
            }

            Expression<BigDecimal> maxVolumeExpr = cb.prod(
                    cb.prod(root.get("length"), root.get("width")),
                    root.get("height")
            );

            switch (contentStatus.toUpperCase()) {
                case "EMPTY":
                    return cb.lessThanOrEqualTo(root.get("currentVolumeUsed"), BigDecimal.ZERO);

                case "PARTIAL":
                    Predicate greaterThanZero = cb.greaterThan(root.get("currentVolumeUsed"), BigDecimal.ZERO);
                    Predicate lessThanMax = cb.lessThan(root.get("currentVolumeUsed"), maxVolumeExpr);
                    return cb.and(greaterThanZero, lessThanMax);

                case "FULL":
                    return cb.greaterThanOrEqualTo(root.get("currentVolumeUsed"), maxVolumeExpr);

                default:
                    return cb.conjunction();
            }
        };
    }

}

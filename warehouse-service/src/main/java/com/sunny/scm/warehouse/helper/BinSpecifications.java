package com.sunny.scm.warehouse.helper;

import com.sunny.scm.warehouse.constant.BinStatus;
import com.sunny.scm.warehouse.constant.BinType;
import com.sunny.scm.warehouse.constant.ZoneType;
import com.sunny.scm.warehouse.entity.Bin;
import com.sunny.scm.warehouse.entity.Zone;
import org.springframework.data.jpa.domain.Specification;

import javax.swing.plaf.SeparatorUI;
import java.time.LocalDate;
import java.util.List;

public class BinSpecifications {
    public static Specification<Bin> belongsToZone(Long zoneId) {
        return (root, query, cb) -> cb.equal(root.get("zone").get("id"), zoneId);
    }

    public static Specification<Bin> likeCodeOrName(String keyword) {
        return SpecificationUtils.orLikeIgnoreCase(List.of("binCode", "binName"), keyword);
    }

    public static Specification<Bin> createdBetween(LocalDate from, LocalDate to) {
        return SpecificationUtils.betweenDate("creationTimestamp", from, to);
    }

    public static Specification<Bin> hasBinType(BinType binType) {
        return (root, query, cb) ->
                binType == null ? cb.conjunction() : cb.equal(root.get("binType"), binType);
    }

    public static Specification<Bin> hasStatus(BinStatus status) {
        return (root, query, cb) ->
                status == null ? cb.conjunction() : cb.equal(root.get("binStatus"), status);
    }
}

package com.sunny.scm.warehouse.helper;

import com.sunny.scm.warehouse.constant.ZoneType;
import com.sunny.scm.warehouse.entity.Zone;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class ZoneSpecifications {
    public static Specification<Zone> belongsToWarehouse(Long warehouseId) {
        return (root, query, cb) -> cb.equal(root.get("warehouse").get("id"), warehouseId);
    }

    public static Specification<Zone> likeCodeOrName(String keyword) {
        return SpecificationUtils.orLikeIgnoreCase(List.of("zoneCode", "zoneName"), keyword);
    }

    public static Specification<Zone> updatedBetween(LocalDate from, LocalDate to) {
        return SpecificationUtils.betweenDate("updateTimestamp", from, to);
    }

    public static Specification<Zone> hasZoneType(ZoneType zoneType) {
        return (root, query, cb) ->
                zoneType == null ? cb.conjunction() : cb.equal(root.get("zoneType"), zoneType);
    }
}

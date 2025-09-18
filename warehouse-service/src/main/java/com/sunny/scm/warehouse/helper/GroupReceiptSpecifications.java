package com.sunny.scm.warehouse.helper;

import com.sunny.scm.warehouse.constant.ReceiptStatus;
import com.sunny.scm.warehouse.entity.GoodReceipt;
import com.sunny.scm.warehouse.entity.GroupReceipt;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class GroupReceiptSpecifications {
    public static Specification<GroupReceipt> belongsToWarehouse(Long warehouseId) {
        return (root, query, cb) -> cb.equal(root.get("warehouse").get("id"), warehouseId);
    }

    public static Specification<GroupReceipt> likeGroupCode(String keyword) {
        return SpecificationUtils.orLikeIgnoreCase(List.of("groupCode"), keyword);
    }


    public static Specification<GroupReceipt> hasStatus(ReceiptStatus status) {
        return (root, query, cb) ->
                status == null ? cb.conjunction() : cb.equal(root.get("receiptStatus"), status);
    }
}

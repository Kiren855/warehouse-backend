package com.sunny.scm.warehouse.repository;

import com.sunny.scm.warehouse.dto.receipt.GroupedPackageDto;
import com.sunny.scm.warehouse.entity.GoodReceiptItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GoodReceiptItemRepository extends JpaRepository<GoodReceiptItem, Long> {

    @Query("SELECT new com.sunny.scm.warehouse.dto.receipt.GroupedPackageDto(" +
            "item.productPackageId, item.expirationDate, SUM(item.packageQuantity)) " +
            "FROM GoodReceiptItem item " +
            "JOIN item.goodReceipt receipt " +
            "JOIN receipt.groupReceipt group " +
            "WHERE group.id = :groupReceiptId " +
            "GROUP BY item.productPackageId, item.expirationDate")
    List<GroupedPackageDto> findGroupedItemsByGroupReceiptId(@Param("groupReceiptId") Long groupReceiptId);
}

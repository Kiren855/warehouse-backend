package com.sunny.scm.warehouse.repository;

import com.sunny.scm.warehouse.dto.receipt.GroupedPackageDto;
import com.sunny.scm.warehouse.entity.GoodReceiptItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GoodReceiptItemRepository extends JpaRepository<GoodReceiptItem, Long> {

    @Query("SELECT new com.sunny.scm.warehouse.dto.receipt.GroupedPackageDto(i.productPackageId, SUM(i.packageQuantity)) " +
            "FROM GoodReceiptItem i " +
            "WHERE i.goodReceipt.groupReceipt.id = :groupReceiptId " +
            "GROUP BY i.productPackageId")
    List<GroupedPackageDto> findGroupedItemsByGroupReceiptId(@Param("groupReceiptId") Long groupReceiptId);

}

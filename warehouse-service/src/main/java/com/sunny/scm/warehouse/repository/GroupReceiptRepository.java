package com.sunny.scm.warehouse.repository;

import com.sunny.scm.warehouse.entity.GoodReceipt;
import com.sunny.scm.warehouse.entity.GroupReceipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GroupReceiptRepository extends JpaRepository<GroupReceipt, Long>,
        JpaSpecificationExecutor<GroupReceipt> {
    Page<GroupReceipt> findAllByWarehouseId(Long warehouseId, int page, int size);
}

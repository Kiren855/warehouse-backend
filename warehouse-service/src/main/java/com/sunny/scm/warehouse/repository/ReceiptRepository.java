package com.sunny.scm.warehouse.repository;

import com.sunny.scm.warehouse.entity.GoodReceipt;
import com.sunny.scm.warehouse.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReceiptRepository extends JpaRepository<GoodReceipt, Long>, JpaSpecificationExecutor<GoodReceipt> {
}

package com.sunny.scm.warehouse.repository;

import com.sunny.scm.warehouse.entity.GoodReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<GoodReceipt, Long> {
}

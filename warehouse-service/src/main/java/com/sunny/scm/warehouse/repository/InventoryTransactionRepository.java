package com.sunny.scm.warehouse.repository;

import com.sunny.scm.warehouse.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {
}

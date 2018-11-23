package com.acme.stock;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface StockRepository extends JpaRepository<Stock, UUID> {

    List<Stock> findByBranch(UUID branch);

    List<Stock> findByProduct(UUID product);

    Stock findByBranchAndProduct(UUID branch, UUID product);
}

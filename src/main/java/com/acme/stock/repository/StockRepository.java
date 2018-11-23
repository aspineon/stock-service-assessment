package com.acme.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StockRepository extends JpaRepository<Stock, UUID> {

    List<Stock> findByProduct(UUID product);

    List<Stock> findByBranch(UUID branch);

    Stock findByProductAndBranch(UUID product, UUID branch);
}

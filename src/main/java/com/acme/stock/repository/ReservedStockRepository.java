package com.acme.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReservedStockRepository extends JpaRepository<ReservedStock, UUID> {

    List<ReservedStock> findByProduct(UUID product);

    List<ReservedStock> findByBranch(UUID branch);

    ReservedStock findByProductAndBranch(UUID product, UUID branch);
}

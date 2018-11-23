package com.acme.reserve;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

interface ReservedStockRepository extends JpaRepository<ReservedStock, UUID> {

    List<ReservedStock> findByProduct(UUID product);

    List<ReservedStock> findByBranch(UUID branch);

    ReservedStock findByProductAndBranch(UUID product, UUID branch);

    List<ReservedStock> findByExpiresBefore(Date date);
}

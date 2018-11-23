package com.acme.reserve;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface ReservedStockRepository extends JpaRepository<ReservedStock, UUID> {

    List<ReservedStock> findByProduct(UUID product);

    List<ReservedStock> findByBranch(UUID branch);

    Optional<ReservedStock> findByProductAndBranch(UUID product, UUID branch);

    List<ReservedStock> findByCreatedBy(String createdBy);

    List<ReservedStock> findByExpiresBefore(Date date);
}

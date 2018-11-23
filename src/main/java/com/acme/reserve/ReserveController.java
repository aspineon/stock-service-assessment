package com.acme.reserve;

import com.acme.reserve.exceptions.ReservedStockNotFound;
import com.acme.stock.exceptions.NotEnoughInStock;
import com.acme.stock.exceptions.StockNotFound;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reserved-stock")
class ReserveController {
    final ReservedStockRepository repository;
    final ReservedStockService service;

    @PostMapping("/reserve")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ReservedStockResponse reserveStock(@RequestBody ReserveStockRequest request) throws StockNotFound, NotEnoughInStock {
        ReservedStock reserved = service.reserve(request);
        return new ReservedStockResponse(reserved);
    }

    @PostMapping("/sell")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void reserveStock(@RequestBody SellRequest request) throws ReservedStockNotFound {
        service.sellReservedStock(request);
    }

    @GetMapping("/find")
    public ReservedStockResponse find(@RequestParam UUID branch, @RequestParam UUID product) throws ReservedStockNotFound {
        return repository.findByBranchAndProduct(branch, product).map(ReservedStockResponse::new)
                .orElseThrow(() -> new ReservedStockNotFound("branch: " + branch + ", product: " + product));
    }

    @GetMapping("/list")
    public List<ReservedStockResponse> listAll() {
        return repository.findAll().stream().map(ReservedStockResponse::new).collect(Collectors.toList());
    }

    /**
     * Allows employees to see the reservations they created.
     *
     * @param createdBy
     * @return
     */
    @GetMapping(value = "/listCreatedBy")
    public List<ReservedStockResponse> listCreatedBy(@RequestParam String createdBy) {
        return repository.findByCreatedBy(createdBy).stream().map(ReservedStockResponse::new).collect(Collectors.toList());
    }
}

@Value
@Validated
class ReserveStockRequest {
    @NotNull
    UUID branch;
    @NotNull
    UUID product;
    @Positive
    int numberOfItems;
}

@Value
class ReservedStockResponse {
    UUID id;
    UUID branch;
    UUID product;
    int numberOfItems;
    String createdBy;
    Date createdDate;

    ReservedStockResponse(ReservedStock reserved) {
        this.id = reserved.getId();
        this.branch = reserved.getBranch();
        this.product = reserved.getProduct();
        this.numberOfItems = reserved.getNumberOfItems();
        this.createdBy = reserved.getCreatedBy();
        this.createdDate = reserved.getCreatedDate();
    }
}

@Value
@Validated
class SellRequest {
    UUID id;
}

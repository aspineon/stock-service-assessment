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
import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reserved-stock")
class ReserveController {
    final ReserveService service;

    @PostMapping(value = "/reserve")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public ReservedStockResponse reserveStock(@RequestBody ReserveStockRequest request) throws StockNotFound, NotEnoughInStock {
        ReservedStock reservedStock = service.reserve(request);
        return new ReservedStockResponse(reservedStock.getId(), reservedStock.getExpires());
    }

    @PostMapping(value = "/sell")
    @ResponseStatus(code = HttpStatus.OK)
    public void reserveStock(@RequestBody SellRequest request) throws ReservedStockNotFound {
        service.sellReservedStock(request);
    }
}

@Value
@Validated
class ReserveStockRequest {
    @NotNull
    UUID product;
    @NotNull
    UUID branch;
    @Positive
    int numberOfItems;
}

@Value
class ReservedStockResponse {
    UUID id;
    Date expires;
}

@Value
@Validated
class SellRequest{
    UUID id;
}
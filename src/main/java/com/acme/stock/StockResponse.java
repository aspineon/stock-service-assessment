package com.acme.stock;

import lombok.Value;

import java.util.UUID;

@Value
class StockResponse {
    final UUID id;
    final UUID branch;
    final UUID product;
    final int numberOfItems;

    StockResponse(Stock stock) {
        this.id = stock.getId();
        this.branch = stock.getBranch();
        this.product = stock.getProduct();
        this.numberOfItems = stock.getNumberOfItems();
    }
}
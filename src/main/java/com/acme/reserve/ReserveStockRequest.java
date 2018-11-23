package com.acme.reserve;

import lombok.Value;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

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
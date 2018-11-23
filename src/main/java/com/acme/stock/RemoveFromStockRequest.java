package com.acme.stock;

import lombok.Value;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Value
@Validated
public class RemoveFromStockRequest {
    @NotNull
    UUID branch;
    @NotNull
    UUID product;
    @Positive
    int numberOfItems;
}
package com.acme.stock;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Entity
@NoArgsConstructor
@Validated
class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NaturalId
    @NotNull
    private UUID product;
    @NaturalId
    @NotNull
    private UUID branch;

    @Min(0)
    private int numberOfItems = 0;
}

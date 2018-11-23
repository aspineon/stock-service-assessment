// tag::sample[]
package com.acme.stock.repository;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ReservedStock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NaturalId
    private UUID product;
    @NaturalId
    private UUID branch;

    private int numberOfItems;
}

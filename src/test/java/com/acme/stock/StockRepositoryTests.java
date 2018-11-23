/*
 * Copyright 2002-2016 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.acme.stock;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@DataJpaTest
public class StockRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StockRepository repository;

    @Rule
    public ExpectedException expect = ExpectedException.none();

    private UUID knownBranch;
    private UUID knownProduct;

    @Before
    public void initStock() {
        knownBranch = UUID.randomUUID();
        knownProduct = UUID.randomUUID();
        // Known branch & product
        store(knownBranch, knownProduct, 6);
        // Unknown branches & products
        for (int i = 0; i < 5; i++) {
            store(UUID.randomUUID(), knownProduct, i);
            store(knownBranch, UUID.randomUUID(), i);
        }
    }

    private void store(UUID branch, UUID product, int numberOfItems) {
        Stock stock = new Stock();
        stock.setBranch(branch);
        stock.setProduct(product);
        stock.setNumberOfItems(numberOfItems);
        entityManager.persist(stock);
    }

    @Test
    public void testCanNotStoreBranchProductTwice() {
        expect.expect(DataIntegrityViolationException.class);
        expect.expectMessage(containsString("UC_STOCK"));

        UUID branch = UUID.randomUUID();
        UUID product = UUID.randomUUID();
        Stock stock1 = new Stock();
        stock1.setBranch(branch);
        stock1.setProduct(product);
        stock1.setNumberOfItems(1);
        repository.save(stock1);
        Stock stock2 = new Stock();
        stock2.setBranch(branch);
        stock2.setProduct(product);
        stock2.setNumberOfItems(2);
        repository.save(stock2);
        repository.flush();
        fail("Should not be allowed to store branch and product combination twice");
    }

    @Test
    public void testFindAll() {
        List<Stock> inStock = repository.findAll();
        assertThat(inStock).hasSize(11);
    }

    @Test
    public void testFindByBranch() {
        List<Stock> inStock = repository.findByBranch(knownBranch);
        assertThat(inStock).extracting(Stock::getBranch).containsOnly(knownBranch).hasSize(6);
    }

    @Test
    public void testFindByProduct() {
        List<Stock> inStock = repository.findByProduct(knownProduct);
        assertThat(inStock).extracting(Stock::getProduct).containsOnly(knownProduct).hasSize(6);
    }
}

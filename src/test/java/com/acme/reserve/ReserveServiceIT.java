package com.acme.reserve;

import com.acme.reserve.exceptions.ReservedStockNotFound;
import com.acme.stock.StockRequest;
import com.acme.stock.StockService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("test")
@Sql
@Transactional
public class ReserveServiceIT {
    @MockBean
    StockService stockService;
    @Autowired
    ReservedStockRepository repository;
    @Autowired
    ReservedStockService service;

    // Taken from /stock-service/src/test/resources/com/acme/reserve/ReserveServiceIT.sql through @Sql above
    UUID branch = UUID.fromString("15336930-43ea-44b3-8414-5b1a379cadc1");
    UUID stockedProduct = UUID.fromString("808e7021-17c2-4bd8-a3fa-c320aa1acbad");
    UUID expiredProduct = UUID.fromString("65e75a7f-8cba-4a16-a7ea-e61785f2f6fc");
    UUID reservedProduct = UUID.fromString("0e960f5f-7924-4771-b65c-b465d7852b89");
    UUID reservedId = UUID.fromString("d32522a5-68bf-44c6-983b-679d46072093");

    @Test
    public void testReturnExpiredToStock() throws Exception {
        service.returnExpiredToStock();

        // Verify stock service has items returned
        verify(stockService).addToStock(new StockRequest(branch, expiredProduct, 12));
        verify(stockService, never()).addToStock(new StockRequest(branch, reservedProduct, 12));

        // Verify repository only contains a single non expired record
        List<ReservedStock> reserved = repository.findAll();
        assertThat(reserved).hasSize(1);
        Date expireCreatedBefore = Date.from(Instant.now().minus(Duration.ofMinutes(30)));
        assertThat(reserved.get(0).getCreatedDate()).isAfter(expireCreatedBefore);
    }

    @Test
    public void testReserve_OK() throws Exception {
        service.reserve(new ReserveStockRequest(branch, stockedProduct, 3));

        // Verify stock service has items removed
        verify(stockService).removeFromStock(new StockRequest(branch, stockedProduct, 3));

        // Verify repository only contains single reserved record
        ReservedStock reservedStock = repository.findByBranchAndProduct(branch, stockedProduct).get();
        assertEquals(3, reservedStock.getNumberOfItems());
    }

    @Test
    public void testSellReservedStock() throws ReservedStockNotFound {
        service.sellReservedStock(new SellRequest(reservedId));

        // Verify removed once sold
        assertFalse(repository.findById(reservedId).isPresent());
    }

}

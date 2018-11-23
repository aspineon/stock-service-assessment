package com.acme.reserve;

import com.acme.stock.RemoveFromStockRequest;
import com.acme.stock.StockService;
import com.acme.stock.exceptions.NotEnoughInStock;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ReserveService {

    final StockService stockService;
    final ReservedStockRepository repository;

    @Scheduled(fixedRateString = "PT1M")
    public void returnToStock() {
        repository.findByExpiresBefore(new Date());
    }

    @Transactional
    public void reserve(ReserveStockRequest request) throws NotEnoughInStock {
        // Ask stockService for availability; remove there, add here, save, and report back to user
        RemoveFromStockRequest removeFromStockRequest = new RemoveFromStockRequest(request.getBranch(), request.getProduct(), request.getNumberOfItems());
        stockService.removeFromStock(removeFromStockRequest);

        ReservedStock reservedStock = new ReservedStock();
        repository.save(reservedStock);
    }
}

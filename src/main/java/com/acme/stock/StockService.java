package com.acme.stock;

import com.acme.stock.exceptions.NotEnoughInStock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {
    final StockRepository repository;

    @Transactional
    public void removeFromStock(RemoveFromStockRequest request) throws NotEnoughInStock {
        Stock stock = repository.findByBranchAndProduct(request.getBranch(), request.getProduct());
        int numberRequested = request.getNumberOfItems();
        int numberInStock = stock.getNumberOfItems();
        if (numberInStock < numberRequested) {
            throw new NotEnoughInStock(request + " exceeds " + stock);
        }
        stock.setNumberOfItems(numberInStock - numberRequested);
        repository.save(stock);
    }
}

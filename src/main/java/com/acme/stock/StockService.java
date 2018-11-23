package com.acme.stock;

import com.acme.stock.exceptions.NotEnoughInStock;
import com.acme.stock.exceptions.StockNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {
    final StockRepository repository;

    @Transactional
    public Stock addToStock(StockRequest addRequest) {
        Stock stock = repository.findByBranchAndProduct(addRequest.getBranch(), addRequest.getProduct())
                .orElseGet(() -> {
                    // Create new records if there is no previous record to update
                    Stock newStock = new Stock();
                    newStock.setBranch(addRequest.getBranch());
                    newStock.setProduct(addRequest.getProduct());
                    return newStock;
                });
        int numberAdded = addRequest.getNumberOfItems();
        int numberOfItems = stock.getNumberOfItems();
        stock.setNumberOfItems(numberOfItems + numberAdded);
        return repository.save(stock);
    }

    @Transactional
    public Stock removeFromStock(StockRequest removeRequest) throws StockNotFound, NotEnoughInStock {
        Stock stock = repository.findByBranchAndProduct(removeRequest.getBranch(), removeRequest.getProduct())
                .orElseThrow(() -> new StockNotFound(removeRequest.toString()));
        int numberRequested = removeRequest.getNumberOfItems();
        int numberInStock = stock.getNumberOfItems();
        if (numberInStock < numberRequested) {
            throw new NotEnoughInStock(removeRequest + " exceeds " + stock);
        }
        stock.setNumberOfItems(numberInStock - numberRequested);
        return repository.save(stock);
    }

    public List<Stock> findAll() {
        // TODO Pageable argument through Spring Data
        return repository.findAll();
    }
}

package com.acme.reserve;

import com.acme.reserve.exceptions.ReservedStockNotFound;
import com.acme.stock.StockRequest;
import com.acme.stock.StockService;
import com.acme.stock.exceptions.NotEnoughInStock;
import com.acme.stock.exceptions.StockNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
public class ReservedStockService {

    final StockService stockService;
    final ReservedStockRepository repository;

    /**
     * On a fixed rate return any expired reserved stocks to normal stock.
     */
    @Scheduled(fixedRateString = "PT1M")
    @Transactional
    public void returnExpiredToStock() {
        Date expireCreatedBefore = Date.from(Instant.now().minus(Duration.ofMinutes(30)));
        for (ReservedStock expired : repository.findByCreatedDateBefore(expireCreatedBefore)) {
            stockService.addToStock(new StockRequest(expired.getBranch(), expired.getProduct(), expired.getNumberOfItems()));
            repository.delete(expired);
        }
    }

    /**
     * When customers request to reserve stock, this method sets the stock aside for a limited period of time.
     *
     * @param request
     * @return
     * @throws NotEnoughInStock
     * @throws StockNotFound
     */
    @Transactional
    public ReservedStock reserve(@NotNull ReserveStockRequest request) throws StockNotFound, NotEnoughInStock {
        // Ask stockService for availability; remove there, add here, save, and report back to user
        StockRequest removeRequest = new StockRequest(request.getBranch(), request.getProduct(), request.getNumberOfItems());
        stockService.removeFromStock(removeRequest);
        // Store served stock with expiration date in the future
        ReservedStock reservedStock = new ReservedStock();
        reservedStock.setBranch(request.getBranch());
        reservedStock.setProduct(request.getProduct());
        reservedStock.setNumberOfItems(request.getNumberOfItems());
        // Return reserved stock with identifier for future reference
        return repository.save(reservedStock);
    }

    /**
     * When customers end up buying the reserved stock this method removes the reservation.
     *
     * @param reservedStock
     * @throws ReservedStockNotFound
     */
    @Transactional
    public void sellReservedStock(@NotNull SellRequest request) throws ReservedStockNotFound {
        if (!repository.existsById(request.getId())) {
            throw new ReservedStockNotFound(request.toString());
        }
        repository.deleteById(request.getId());
        // XXX Consider publishing an event here if we don't end up using Spring Data @DomainEvents
    }

    public List<ReservedStock> findAll() {
        // TODO Pageable argument through Spring Data
        return repository.findAll();
    }
}

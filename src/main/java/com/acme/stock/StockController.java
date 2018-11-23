package com.acme.stock;

import com.acme.stock.exceptions.NotEnoughInStock;
import com.acme.stock.exceptions.StockNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stock")
class StockController {
    final StockRepository repository;
    final StockService service;

    @PostMapping(value = "/add")
    @ResponseStatus(code = HttpStatus.OK)
    public StockResponse add(@RequestBody StockRequest request) {
        Stock stock = service.addToStock(request);
        return new StockResponse(stock);
    }

    @PostMapping(value = "/remove")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public StockResponse remove(@RequestBody StockRequest request) throws StockNotFound, NotEnoughInStock {
        Stock stock = service.removeFromStock(request);
        return new StockResponse(stock);
    }

    @GetMapping("/find")
    public StockResponse find(@RequestParam UUID branch, @RequestParam UUID product) throws StockNotFound {
        return repository.findByBranchAndProduct(branch, product).map(StockResponse::new)
                .orElseThrow(() -> new StockNotFound("branch: " + branch + ", product: " + product));
    }

    @GetMapping("/list")
    public List<StockResponse> listAll() {
        return repository.findAll().stream().map(StockResponse::new).collect(Collectors.toList());
    }
}

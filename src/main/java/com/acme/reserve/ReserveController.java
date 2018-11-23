package com.acme.reserve;

import com.acme.stock.exceptions.NotEnoughInStock;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reserve")
@RequiredArgsConstructor
class ReserveController {
    final ReserveService service;

    @PostMapping(value = "/stock")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void postMethodName(@RequestBody ReserveStockRequest request) throws NotEnoughInStock {
        service.reserve(request);
    }
}

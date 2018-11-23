package com.acme.stock.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Stock not found")
public class StockNotFound extends Exception {
    public StockNotFound(String message) {
        super(message);
    }
}

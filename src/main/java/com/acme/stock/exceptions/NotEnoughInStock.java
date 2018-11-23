package com.acme.stock.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Not enough items in stock")
public class NotEnoughInStock extends Exception {
    public NotEnoughInStock(String message) {
        super(message);
    }
}

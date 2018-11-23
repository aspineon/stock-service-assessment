package com.acme.reserve.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Reserved stock not found")
public class ReservedStockNotFound extends Exception {
    public ReservedStockNotFound(String message) {
        super(message);
    }
}

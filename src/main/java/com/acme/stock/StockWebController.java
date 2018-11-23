package com.acme.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
class StockWebController {
    final StockService service;

    @GetMapping("/stocks")
    public String stocks(Model model) {
        model.addAttribute("name", "testuser");
        List<StockResponse> stockResponses = service.findAll().stream().map(StockResponse::new).collect(Collectors.toList());
        model.addAttribute("reserved", stockResponses);
        return "stocks";
    }

}

package com.acme.reserve;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
class ReservedStockWebController {
    final ReservedStockService service;

    @GetMapping("/stocks")
    public String stocks(Model model) {
        model.addAttribute("name", "testuser");
        List<ReservedStockResponse> stockResponses = service.findAll().stream().map(ReservedStockResponse::new).collect(Collectors.toList());
        model.addAttribute("stocks", stockResponses);
        return "stocks";
    }

}

package com.acme.reserve;

import com.acme.reserve.exceptions.ReservedStockNotFound;
import com.acme.stock.exceptions.NotEnoughInStock;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ReserveController.class)
public class ReserveControllerTest {
    @MockBean
    ReserveService service;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mockMvc;

    @Test
    public void testReserveOk() throws Exception {
        ReserveStockRequest request = new ReserveStockRequest(UUID.randomUUID(), UUID.randomUUID(), 1);
        ReservedStock reservedStock = new ReservedStock();
        reservedStock.setId(UUID.randomUUID());
        reservedStock.setExpires(new Date());
        when(service.reserve(request)).thenReturn(reservedStock);

        mockMvc.perform(post("/reserved-stock/reserve")
                .content(mapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(reservedStock.getId().toString()))
                .andExpect(jsonPath("$.expires").value(reservedStock.getExpires()));
        verify(service).reserve(request);
    }

    @Test
    public void testReserve_NotEnoughInStock() throws Exception {
        ReserveStockRequest request = new ReserveStockRequest(UUID.randomUUID(), UUID.randomUUID(), 1000);
        when(service.reserve(request)).thenThrow(new NotEnoughInStock("I have, like, 12"));
        mockMvc.perform(post("/reserved-stock/reserve")
                .content(mapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Not enough items in stock"));
    }

    @Test
    public void testSellOk() throws Exception {
        SellRequest request = new SellRequest(UUID.randomUUID());
        mockMvc.perform(post("/reserved-stock/sell")
                .content(mapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(service).sellReservedStock(request);
    }

    @Test
    public void testSell_NotFound() throws Exception {
        SellRequest request = new SellRequest(UUID.randomUUID());
        doThrow(new ReservedStockNotFound("Reserved three weeks ago you said?")).when(service).sellReservedStock(request);

        mockMvc.perform(post("/reserved-stock/sell")
                .content(mapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Reserved stock not found"));
    }

}

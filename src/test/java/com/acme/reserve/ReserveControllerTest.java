package com.acme.reserve;

import com.acme.reserve.exceptions.ReservedStockNotFound;
import com.acme.stock.exceptions.NotEnoughInStock;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ReserveController.class)
public class ReserveControllerTest {
    @MockBean
    ReservedStockRepository repository;
    @MockBean
    ReservedStockService service;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mockMvc;

    @MockBean
    MappingContext mappingContext; // Needed for jpaAuditingHandler

    @Test
    public void testReserveOk() throws Exception {
        ReserveStockRequest request = new ReserveStockRequest(UUID.randomUUID(), UUID.randomUUID(), 1);
        ReservedStock reservedStock = new ReservedStock();
        reservedStock.setId(UUID.randomUUID());
        reservedStock.setCreatedDate(new Date());
        when(service.reserve(request)).thenReturn(reservedStock);

        mockMvc.perform(post("/reserved-stock/reserve")
                .content(mapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(reservedStock.getId().toString()))
                .andExpect(jsonPath("$.createdDate").value(reservedStock.getCreatedDate()));
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
                .andExpect(status().isNoContent());
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

    @Test
    public void testFindOk() throws Exception {
        UUID branch = UUID.randomUUID();
        UUID product = UUID.randomUUID();
        ReservedStock response = new ReservedStock();
        when(repository.findByBranchAndProduct(branch, product)).thenReturn(Optional.of(response));
        mockMvc.perform(get("/reserved-stock/find")
                .param("branch", branch.toString())
                .param("product", product.toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void testFind_NotFound() throws Exception {
        UUID branch = UUID.randomUUID();
        UUID product = UUID.randomUUID();
        when(repository.findByBranchAndProduct(branch, product)).thenReturn(Optional.empty());
        mockMvc.perform(get("/reserved-stock/find")
                .param("branch", branch.toString())
                .param("product", product.toString()))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Reserved stock not found"));
    }

    @Test
    public void testListOk() throws Exception {
        UUID branch = UUID.randomUUID();
        UUID product = UUID.randomUUID();
        ReservedStock response = new ReservedStock();
        when(repository.findAll()).thenReturn(Collections.singletonList(response));
        mockMvc.perform(get("/reserved-stock/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testList_empty() throws Exception {
        UUID branch = UUID.randomUUID();
        UUID product = UUID.randomUUID();
        when(repository.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/reserved-stock/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}

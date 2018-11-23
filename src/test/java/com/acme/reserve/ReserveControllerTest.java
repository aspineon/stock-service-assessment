package com.acme.reserve;

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

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    public void testNotEnoughInStock() throws Exception {
        doThrow(new NotEnoughInStock("I have, like, 12")).when(service).reserve(any(ReserveStockRequest.class));

        ReserveStockRequest request = new ReserveStockRequest(UUID.randomUUID(), UUID.randomUUID(), 1000);
        mockMvc.perform(post("/reserve/stock")
                .content(mapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Not enough items in stock"));
    }

    @Test
    public void testReserveOk() throws Exception {
        ReserveStockRequest request = new ReserveStockRequest(UUID.randomUUID(), UUID.randomUUID(), 1);
        mockMvc.perform(post("/reserve/stock")
                .content(mapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
        verify(service).reserve(request);
    }

}

package com.alperentuncer.payment.saga.stock_service.controller;

import com.alperentuncer.payment.saga.stock_service.api.GlobalExceptionHandler;
import com.alperentuncer.payment.saga.stock_service.api.dto.ReserveStockRequest;
import com.alperentuncer.payment.saga.stock_service.domain.ReservationItem;
import com.alperentuncer.payment.saga.stock_service.domain.StockReservation;
import com.alperentuncer.payment.saga.stock_service.service.StockService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StockController.class)
@Import(GlobalExceptionHandler.class)
class StockControllerWebTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    StockService stockService;

    @Test
    void reserve_should_return_ok_and_body() throws Exception {
        var orderId = UUID.randomUUID();
        var reservation = StockReservation.request(orderId,
                List.of(ReservationItem.of(UUID.randomUUID(), 2)));
        reservation.markReserved();

        Mockito.when(stockService.reserve(any()))
                .thenReturn(reservation);

        var json = """
                {
                  "orderId": "%s",
                  "items": [
                    { "productId": "%s", "quantity": 2 }
                  ]
                }
                """.formatted(orderId, UUID.randomUUID());

        mvc.perform(post("/api/v1/stock/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(orderId.toString()))
                .andExpect(jsonPath("$.status").value("RESERVED"));
    }

    @Test
    void reserve_validation_error_should_return_400() throws Exception {
        // items boş → validasyon hatası
        var json = """
                { "orderId": "%s", "items": [] }
                """.formatted(UUID.randomUUID());

        mvc.perform(post("/api/v1/stock/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }
}

package com.alperentuncer.payment.saga.stock_service.controller;

import com.alperentuncer.payment.saga.stock_service.api.dto.ReservationResponse;
import com.alperentuncer.payment.saga.stock_service.api.dto.ReserveStockRequest;
import com.alperentuncer.payment.saga.stock_service.api.mapper.StockApiMapper;
import com.alperentuncer.payment.saga.stock_service.service.StockService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stock")
public class StockController {

    private final StockService stockService;
    public StockController(StockService stockService) { this.stockService = stockService; }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> reserve(@Valid @RequestBody ReserveStockRequest request) {
        var cmd = StockApiMapper.toCommand(request);
        var reservation = stockService.reserve(cmd);
        return ResponseEntity.ok(StockApiMapper.toResponse(reservation));
    }

    @PostMapping("/reservations/{orderId}/commit")
    public ResponseEntity<ReservationResponse> commit(@PathVariable UUID orderId) {
        var r = stockService.commit(orderId);
        return ResponseEntity.ok(StockApiMapper.toResponse(r));
    }

    @PostMapping("/reservations/{orderId}/release")
    public ResponseEntity<ReservationResponse> release(@PathVariable UUID orderId) {
        var r = stockService.release(orderId);
        return ResponseEntity.ok(StockApiMapper.toResponse(r));
    }
}

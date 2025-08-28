package com.alperentuncer.payment.saga.stock_service.service;

import com.alperentuncer.payment.saga.stock_service.domain.StockReservation;

import java.util.UUID;

public interface StockService {
    StockReservation reserve(ReserveStockCommand command);
    StockReservation commit(UUID orderId);
    StockReservation release(UUID orderId);
}

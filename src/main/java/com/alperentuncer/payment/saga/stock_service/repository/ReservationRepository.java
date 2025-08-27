package com.alperentuncer.payment.saga.stock_service.repository;

import java.util.Optional;
import java.util.UUID;

import com.alperentuncer.payment.saga.stock_service.domain.StockReservation;

public interface ReservationRepository {
    StockReservation save(StockReservation reservation);
    Optional<StockReservation> findByOrderId(UUID orderId);
}

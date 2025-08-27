package com.alperentuncer.payment.saga.stock_service.persistence.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alperentuncer.payment.saga.stock_service.persistence.entity.StockReservationEntity;

public interface ReservationJpaRepository extends JpaRepository<StockReservationEntity, UUID> {
    Optional<StockReservationEntity> findByOrderId(UUID orderId);
}

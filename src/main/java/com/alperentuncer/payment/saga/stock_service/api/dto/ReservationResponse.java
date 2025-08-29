package com.alperentuncer.payment.saga.stock_service.api.dto;

import com.alperentuncer.payment.saga.stock_service.domain.ReservationStatus;

import java.time.Instant;
import java.util.UUID;

public record ReservationResponse(
        UUID id,
        UUID orderId,
        ReservationStatus status,
        Instant createdAt
) {}

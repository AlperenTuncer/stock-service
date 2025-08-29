package com.alperentuncer.payment.saga.stock_service.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record ReserveStockRequest(
        @NotNull UUID orderId,
        @NotNull @Size(min = 1) List<@Valid Item> items
) {
    public record Item(
            @NotNull UUID productId,
            @Min(1) long quantity
    ) {}
}

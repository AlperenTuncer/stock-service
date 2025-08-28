package com.alperentuncer.payment.saga.stock_service.service;

import java.util.List;
import java.util.UUID;

public record ReserveStockCommand(
        UUID orderId,
        List<Item> items
) {
    public record Item(UUID productId, long quantity) {}
}

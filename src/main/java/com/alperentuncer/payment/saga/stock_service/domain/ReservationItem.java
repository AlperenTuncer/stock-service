package com.alperentuncer.payment.saga.stock_service.domain;

import java.util.Objects;
import java.util.UUID;

public final class ReservationItem {
    private final UUID productId;
    private final long quantity;

    private ReservationItem(UUID productId, long quantity) {
        if (productId == null) throw new IllegalArgumentException("productId zorunlu");
        if (quantity < 1) throw new IllegalArgumentException("quantity >= 1 olmalı");
        this.productId = productId;
        this.quantity = quantity;
    }

    public static ReservationItem of(UUID productId, long quantity) {
        return new ReservationItem(productId, quantity);
    }

    public UUID getProductId() { return productId; }
    public long getQuantity() { return quantity; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationItem that)) return false;
        return quantity == that.quantity && Objects.equals(productId, that.productId);
    }
    @Override public int hashCode() { return Objects.hash(productId, quantity); }
}
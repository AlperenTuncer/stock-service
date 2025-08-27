package com.alperentuncer.payment.saga.stock_service.persistence.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ReservationItemEmbeddable {
    @Column(name="product_id", nullable = false)
    private UUID productId;

    @Column(name="quantity", nullable = false)
    private long quantity;

    protected ReservationItemEmbeddable() {}

    public ReservationItemEmbeddable(UUID productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public UUID getProductId() { return productId; }
    public long getQuantity() { return quantity; }
}

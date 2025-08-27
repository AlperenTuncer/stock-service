package com.alperentuncer.payment.saga.stock_service.persistence.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name="available_qty", nullable = false)
    private long availableQty;

    protected ProductEntity() {}

    public ProductEntity(UUID id, String name, long availableQty) {
        this.id = id;
        this.name = name;
        this.availableQty = availableQty;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public long getAvailableQty() { return availableQty; }
    public void setAvailableQty(long q) { this.availableQty = q; }
}

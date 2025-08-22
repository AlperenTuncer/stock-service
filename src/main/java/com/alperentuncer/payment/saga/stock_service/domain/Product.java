package com.alperentuncer.payment.saga.stock_service.domain;

import java.util.UUID;

public class Product {
    private final UUID id;
    private final String name;
    private long availableQty;

    private Product(UUID id, String name, long availableQty) {
        if (id == null) throw new IllegalArgumentException("id zorunlu");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name zorunlu");
        if (availableQty < 0) throw new IllegalArgumentException("availableQty negatif olamaz");
        this.id = id;
        this.name = name;
        this.availableQty = availableQty;
    }

    public static Product create(UUID id, String name, long availableQty) {
        return new Product(id, name, availableQty);
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public long getAvailableQty() { return availableQty; }

    /** Commit aşamasında düşüm yapılır */
    public void decrement(long qty) {
        if (qty < 1) throw new IllegalArgumentException("qty >= 1 olmalı");
        if (availableQty - qty < 0) throw new IllegalStateException("yetersiz stok");
        availableQty -= qty;
    }
}
package com.alperentuncer.payment.saga.stock_service.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class StockReservation {
    private final UUID id;
    private final UUID orderId;
    private final Instant createdAt;
    private ReservationStatus status;
    private final List<ReservationItem> items;

    private StockReservation(UUID id, UUID orderId, List<ReservationItem> items,
                             ReservationStatus status, Instant createdAt) {
        if (orderId == null) throw new IllegalArgumentException("orderId zorunlu");
        if (items == null || items.isEmpty()) throw new IllegalArgumentException("en az 1 satır");
        this.id = (id != null) ? id : UUID.randomUUID();
        this.orderId = orderId;
        this.items = new ArrayList<>(items);
        this.status = (status != null) ? status : ReservationStatus.REQUESTED;
        this.createdAt = (createdAt != null) ? createdAt : Instant.now();
    }

    public static StockReservation request(UUID orderId, List<ReservationItem> items) {
        return new StockReservation(null, orderId, items, ReservationStatus.REQUESTED, null);
    }

    public static StockReservation rehydrate(UUID id, UUID orderId, List<ReservationItem> items,
                                             ReservationStatus status, Instant createdAt) {
        return new StockReservation(id, orderId, items, status, createdAt);
    }

    public UUID getId() { return id; }
    public UUID getOrderId() { return orderId; }
    public Instant getCreatedAt() { return createdAt; }
    public ReservationStatus getStatus() { return status; }
    public List<ReservationItem> getItems() { return Collections.unmodifiableList(items); }

    public void markReserved() {
        if (status != ReservationStatus.REQUESTED) throw new IllegalStateException("yalnızca REQUESTED → RESERVED");
        status = ReservationStatus.RESERVED;
    }
    public void markRejected() {
        if (status != ReservationStatus.REQUESTED) throw new IllegalStateException("yalnızca REQUESTED → REJECTED");
        status = ReservationStatus.REJECTED;
    }
    public void commit() {
        if (status != ReservationStatus.RESERVED) throw new IllegalStateException("yalnızca RESERVED → COMMITTED");
        status = ReservationStatus.COMMITTED;
    }
    public void release() {
        if (status != ReservationStatus.RESERVED) throw new IllegalStateException("yalnızca RESERVED → RELEASED");
        status = ReservationStatus.RELEASED;
    }
}
package com.alperentuncer.payment.saga.stock_service.persistence.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.alperentuncer.payment.saga.stock_service.domain.ReservationStatus;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "stock_reservations")
public class StockReservationEntity {

    @Id
    private UUID id;

    @Column(name="order_id", nullable = false, unique = true)
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @Column(name="created_at", nullable = false)
    private Instant createdAt;

    @ElementCollection
    @CollectionTable(name = "reservation_items", joinColumns = @JoinColumn(name = "reservation_id"))
    private List<ReservationItemEmbeddable> items = new ArrayList<>();

    protected StockReservationEntity() {}

    public StockReservationEntity(UUID id, UUID orderId, ReservationStatus status,
                                  Instant createdAt, List<ReservationItemEmbeddable> items) {
        this.id = id;
        this.orderId = orderId;
        this.status = status;
        this.createdAt = createdAt;
        this.items = items;
    }

    public UUID getId() { return id; }
    public UUID getOrderId() { return orderId; }
    public ReservationStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public List<ReservationItemEmbeddable> getItems() { return items; }
}

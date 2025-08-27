package com.alperentuncer.payment.saga.stock_service.persistence.mapper;

import java.util.List;

import com.alperentuncer.payment.saga.stock_service.domain.Product;
import com.alperentuncer.payment.saga.stock_service.domain.ReservationItem;
import com.alperentuncer.payment.saga.stock_service.domain.StockReservation;
import com.alperentuncer.payment.saga.stock_service.persistence.entity.ProductEntity;
import com.alperentuncer.payment.saga.stock_service.persistence.entity.ReservationItemEmbeddable;
import com.alperentuncer.payment.saga.stock_service.persistence.entity.StockReservationEntity;

public final class StockMapper {
    private StockMapper(){}

    // Product
    public static ProductEntity toEntity(Product p) {
        return new ProductEntity(p.getId(), p.getName(), p.getAvailableQty());
    }
    public static Product toDomain(ProductEntity e) {
        return Product.create(e.getId(), e.getName(), e.getAvailableQty());
    }

    // Reservation
    public static StockReservationEntity toEntity(StockReservation r) {
        var items = r.getItems().stream()
                .map(i -> new ReservationItemEmbeddable(i.getProductId(), i.getQuantity()))
                .toList();
        return new StockReservationEntity(r.getId(), r.getOrderId(), r.getStatus(), r.getCreatedAt(), items);
    }
    public static StockReservation toDomain(StockReservationEntity e) {
        List<ReservationItem> items = e.getItems().stream()
                .map(i -> ReservationItem.of(i.getProductId(), i.getQuantity()))
                .toList();
        return StockReservation.rehydrate(e.getId(), e.getOrderId(), items, e.getStatus(), e.getCreatedAt());
    }
}

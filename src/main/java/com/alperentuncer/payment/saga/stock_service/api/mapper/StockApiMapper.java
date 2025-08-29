package com.alperentuncer.payment.saga.stock_service.api.mapper;

import com.alperentuncer.payment.saga.stock_service.api.dto.ReservationResponse;
import com.alperentuncer.payment.saga.stock_service.api.dto.ReserveStockRequest;
import com.alperentuncer.payment.saga.stock_service.domain.StockReservation;
import com.alperentuncer.payment.saga.stock_service.service.ReserveStockCommand;

public final class StockApiMapper {
    private StockApiMapper(){}

    public static ReserveStockCommand toCommand(ReserveStockRequest req) {
        var items = req.items().stream()
                .map(i -> new ReserveStockCommand.Item(i.productId(), i.quantity()))
                .toList();
        return new ReserveStockCommand(req.orderId(), items);
    }

    public static ReservationResponse toResponse(StockReservation r) {
        return new ReservationResponse(r.getId(), r.getOrderId(), r.getStatus(), r.getCreatedAt());
    }
}

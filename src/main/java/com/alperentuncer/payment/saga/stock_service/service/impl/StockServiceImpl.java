package com.alperentuncer.payment.saga.stock_service.service.impl;

import com.alperentuncer.payment.saga.stock_service.domain.Product;
import com.alperentuncer.payment.saga.stock_service.domain.ReservationItem;
import com.alperentuncer.payment.saga.stock_service.domain.StockReservation;
import com.alperentuncer.payment.saga.stock_service.repository.ProductRepository;
import com.alperentuncer.payment.saga.stock_service.repository.ReservationRepository;
import com.alperentuncer.payment.saga.stock_service.service.ReserveStockCommand;
import com.alperentuncer.payment.saga.stock_service.service.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
public class StockServiceImpl implements StockService {

    private final ProductRepository productRepository;
    private final ReservationRepository reservationRepository;

    public StockServiceImpl(ProductRepository productRepository, ReservationRepository reservationRepository) {
        this.productRepository = productRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public StockReservation reserve(ReserveStockCommand command) {
        if (command == null) throw new IllegalArgumentException("command zorunlu");
        if (command.orderId() == null) throw new IllegalArgumentException("orderId zorunlu");
        if (command.items() == null || command.items().isEmpty())
            throw new IllegalArgumentException("en az 1 satır");

        // Tüm kalemler için availability kontrolü (MVP: sadece kontrol, hemen düşme yok)
        for (var it : command.items()) {
            var p = productRepository.findById(it.productId())
                    .orElseThrow(() -> new IllegalArgumentException("ürün bulunamadı: " + it.productId()));
            if (p.getAvailableQty() < it.quantity()) {
                var rejected = StockReservation.request(command.orderId(),
                        command.items().stream().map(i -> ReservationItem.of(i.productId(), i.quantity())).toList());
                rejected.markRejected();
                return reservationRepository.save(rejected);
            }
        }

        var reserved = StockReservation.request(command.orderId(),
                command.items().stream().map(i -> ReservationItem.of(i.productId(), i.quantity())).toList());
        reserved.markReserved();
        return reservationRepository.save(reserved);
    }

    @Override
    public StockReservation commit(UUID orderId) {
        Objects.requireNonNull(orderId, "orderId zorunlu");
        var r = reservationRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("rezervasyon yok: " + orderId));

        // Sadece RESERVED → COMMITTED
        r.commit();

        // Ürün adetlerini burada düş
        for (var it : r.getItems()) {
            Product p = productRepository.findById(it.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("ürün yok: " + it.getProductId()));
            p.decrement(it.getQuantity());
            productRepository.save(p);
        }
        return reservationRepository.save(r);
    }

    @Override
    public StockReservation release(UUID orderId) {
        Objects.requireNonNull(orderId, "orderId zorunlu");
        var r = reservationRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("rezervasyon yok: " + orderId));

        // Sadece RESERVED → RELEASED
        r.release();
        return reservationRepository.save(r);
    }
}

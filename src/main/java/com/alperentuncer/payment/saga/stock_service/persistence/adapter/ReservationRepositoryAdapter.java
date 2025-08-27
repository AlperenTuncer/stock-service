package com.alperentuncer.payment.saga.stock_service.persistence.adapter;

import com.alperentuncer.payment.saga.stock_service.domain.StockReservation;
import com.alperentuncer.payment.saga.stock_service.persistence.jpa.ReservationJpaRepository;
import com.alperentuncer.payment.saga.stock_service.persistence.mapper.StockMapper;
import com.alperentuncer.payment.saga.stock_service.repository.ReservationRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
 
@Repository
@Transactional
public class ReservationRepositoryAdapter implements ReservationRepository {

    private final ReservationJpaRepository jpa;

    public ReservationRepositoryAdapter(ReservationJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public StockReservation save(StockReservation reservation) {
        var saved = jpa.save(StockMapper.toEntity(reservation));
        return StockMapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StockReservation> findByOrderId(UUID orderId) {
        return jpa.findByOrderId(orderId).map(StockMapper::toDomain);
    }
}

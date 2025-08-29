package com.alperentuncer.payment.saga.stock_service.persistence;

import com.alperentuncer.payment.saga.stock_service.domain.ReservationItem;
import com.alperentuncer.payment.saga.stock_service.domain.StockReservation;
import com.alperentuncer.payment.saga.stock_service.persistence.adapter.ReservationRepositoryAdapter;
import com.alperentuncer.payment.saga.stock_service.persistence.jpa.ReservationJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class ReservationJpaSliceTest {

    @Autowired
    ReservationJpaRepository jpa;

    @Test
    void save_and_findByOrderId_should_work() {
        var adapter = new ReservationRepositoryAdapter(jpa);

        var orderId = UUID.randomUUID();
        var r = StockReservation.request(orderId,
                List.of(ReservationItem.of(UUID.randomUUID(), 2)));

        r.markReserved();
        var saved = adapter.save(r);

        var found = adapter.findByOrderId(orderId).orElseThrow();
        assertThat(found.getId()).isEqualTo(saved.getId());
        assertThat(found.getStatus().name()).isEqualTo("RESERVED");
        assertThat(found.getItems()).hasSize(1);
    }
}

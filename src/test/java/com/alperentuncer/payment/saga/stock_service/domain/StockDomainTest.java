package com.alperentuncer.payment.saga.stock_service.domain;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class StockDomainTest {

    @Test
    void request_then_markReserved_should_set_status_reserved() {
        var items = List.of(ReservationItem.of(UUID.randomUUID(), 2));
        var r = StockReservation.request(UUID.randomUUID(), items);

        r.markReserved();

        assertThat(r.getStatus()).isEqualTo(ReservationStatus.RESERVED);
        assertThat(r.getItems()).hasSize(1);
    }

    @Test
    void request_then_markRejected_should_set_status_rejected() {
        var items = List.of(ReservationItem.of(UUID.randomUUID(), 1));
        var r = StockReservation.request(UUID.randomUUID(), items);

        r.markRejected();

        assertThat(r.getStatus()).isEqualTo(ReservationStatus.REJECTED);
    }

    @Test
    void commit_only_from_reserved_should_work() {
        var items = List.of(ReservationItem.of(UUID.randomUUID(), 1));
        var r = StockReservation.request(UUID.randomUUID(), items);
        r.markReserved();

        r.commit();

        assertThat(r.getStatus()).isEqualTo(ReservationStatus.COMMITTED);
    }

    @Test
    void release_only_from_reserved_should_work() {
        var items = List.of(ReservationItem.of(UUID.randomUUID(), 1));
        var r = StockReservation.request(UUID.randomUUID(), items);
        r.markReserved();

        r.release();

        assertThat(r.getStatus()).isEqualTo(ReservationStatus.RELEASED);
    }

    @Test
    void product_decrement_should_fail_when_insufficient() {
        var p = Product.create(UUID.randomUUID(), "Mouse", 1);
        assertThatThrownBy(() -> p.decrement(2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("yetersiz");
    }
}

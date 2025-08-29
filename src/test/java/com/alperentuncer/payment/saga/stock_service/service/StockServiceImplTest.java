package com.alperentuncer.payment.saga.stock_service.service;

import com.alperentuncer.payment.saga.stock_service.domain.Product;
import com.alperentuncer.payment.saga.stock_service.domain.ReservationItem;
import com.alperentuncer.payment.saga.stock_service.domain.ReservationStatus;
import com.alperentuncer.payment.saga.stock_service.domain.StockReservation;
import com.alperentuncer.payment.saga.stock_service.repository.ProductRepository;
import com.alperentuncer.payment.saga.stock_service.repository.ReservationRepository;
import com.alperentuncer.payment.saga.stock_service.service.impl.StockServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class StockServiceImplTest {

    private ProductRepository productRepository;
    private ReservationRepository reservationRepository;
    private StockService stockService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        reservationRepository = mock(ReservationRepository.class);
        stockService = new StockServiceImpl(productRepository, reservationRepository);
    }

    @Test
    void reserve_should_return_reserved_when_all_items_available() {
        var orderId = UUID.randomUUID();
        var productId = UUID.randomUUID();

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(Product.create(productId, "Laptop", 10)));
        when(reservationRepository.save(any(StockReservation.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        var cmd = new ReserveStockCommand(orderId,
                List.of(new ReserveStockCommand.Item(productId, 2)));

        var res = stockService.reserve(cmd);

        assertThat(res.getStatus()).isEqualTo(ReservationStatus.RESERVED);
        verify(reservationRepository).save(any(StockReservation.class));
    }

    @Test
    void reserve_should_return_rejected_when_any_item_insufficient() {
        var orderId = UUID.randomUUID();
        var productId = UUID.randomUUID();

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(Product.create(productId, "Mouse", 1)));
        when(reservationRepository.save(any(StockReservation.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        var cmd = new ReserveStockCommand(orderId,
                List.of(new ReserveStockCommand.Item(productId, 5)));

        var res = stockService.reserve(cmd);

        assertThat(res.getStatus()).isEqualTo(ReservationStatus.REJECTED);
    }

    @Test
    void commit_should_decrement_products_and_set_committed() {
        var orderId = UUID.randomUUID();
        var p1 = UUID.randomUUID();

        var reservation = StockReservation.request(orderId,
                List.of(ReservationItem.of(p1, 3)));
        reservation.markReserved();

        when(reservationRepository.findByOrderId(orderId))
                .thenReturn(Optional.of(reservation));
        when(productRepository.findById(p1))
                .thenReturn(Optional.of(Product.create(p1, "Keyboard", 10)));
        when(productRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(reservationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var res = stockService.commit(orderId);

        assertThat(res.getStatus()).isEqualTo(ReservationStatus.COMMITTED);
        verify(productRepository, times(1)).save(any());
        verify(reservationRepository, times(1)).save(any());
    }

    @Test
    void release_should_set_released_when_reserved() {
        var orderId = UUID.randomUUID();
        var reservation = StockReservation.request(orderId,
                List.of(ReservationItem.of(UUID.randomUUID(), 1)));
        reservation.markReserved();

        when(reservationRepository.findByOrderId(orderId))
                .thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var res = stockService.release(orderId);

        assertThat(res.getStatus()).isEqualTo(ReservationStatus.RELEASED);
        verify(reservationRepository).save(any());
    }
}

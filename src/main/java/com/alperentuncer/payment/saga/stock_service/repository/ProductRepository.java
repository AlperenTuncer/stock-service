package com.alperentuncer.payment.saga.stock_service.repository;

import java.util.Optional;
import java.util.UUID;

import com.alperentuncer.payment.saga.stock_service.domain.Product;

public interface ProductRepository {
    Product save(Product p);
    Optional<Product> findById(UUID id);
}

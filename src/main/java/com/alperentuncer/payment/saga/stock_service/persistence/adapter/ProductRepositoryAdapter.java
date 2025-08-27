package com.alperentuncer.payment.saga.stock_service.persistence.adapter;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.alperentuncer.payment.saga.stock_service.domain.Product;
import com.alperentuncer.payment.saga.stock_service.persistence.jpa.ProductJpaRepository;
import com.alperentuncer.payment.saga.stock_service.persistence.mapper.StockMapper;
import com.alperentuncer.payment.saga.stock_service.repository.ProductRepository;

@Repository
@Transactional
public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductJpaRepository jpa;

    public ProductRepositoryAdapter(ProductJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Product save(Product p) {
        var saved = jpa.save(StockMapper.toEntity(p));
        return StockMapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(UUID id) {
        return jpa.findById(id).map(StockMapper::toDomain);
    }
}

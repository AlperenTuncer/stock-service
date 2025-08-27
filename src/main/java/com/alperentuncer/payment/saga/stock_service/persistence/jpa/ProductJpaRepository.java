package com.alperentuncer.payment.saga.stock_service.persistence.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alperentuncer.payment.saga.stock_service.persistence.entity.ProductEntity;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> { }

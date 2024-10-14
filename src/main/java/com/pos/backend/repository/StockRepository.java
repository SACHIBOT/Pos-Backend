package com.pos.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pos.backend.entity.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Stock findByItemItemId(Long itemId);
}

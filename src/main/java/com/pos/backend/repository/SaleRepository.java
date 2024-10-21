package com.pos.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pos.backend.entity.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

}
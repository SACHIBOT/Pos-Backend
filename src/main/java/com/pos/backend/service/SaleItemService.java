package com.pos.backend.service;

import com.pos.backend.entity.SaleItem;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface SaleItemService {
    SaleItem getSaleItem(Long saleItemId);

    List<SaleItem> getSaleItemsBySaleId(Long saleId);

    List<SaleItem> getAllSales();

    void removeSaleItem(Long saleItemId);
}

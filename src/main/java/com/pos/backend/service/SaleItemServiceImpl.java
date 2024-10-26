package com.pos.backend.service;

import com.pos.backend.entity.SaleItem;
import com.pos.backend.repository.SaleItemRepository;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaleItemServiceImpl implements SaleItemService {

    @Autowired
    private SaleItemRepository saleItemRepository;

    @Override
    public List<SaleItem> getSaleItemsBySaleId(Long saleId) {
        return saleItemRepository.findBySaleSaleId(saleId).orElse(null);
    }

    @Override
    public SaleItem getSaleItem(Long id) {
        return saleItemRepository.findById(id).orElse(null);
    }

    @Override
    public List<SaleItem> getAllSales() {
        return saleItemRepository.findAll();
    }

    @Override
    @Transactional
    public void removeSaleItem(Long saleItemId) {
        SaleItem saleItem = saleItemRepository.findById(saleItemId).orElse(null);
        if (saleItem != null) {
            saleItemRepository.delete(saleItem);
        }
    }

}

package com.pos.backend.service;

import com.pos.backend.entity.SaleItem;
import com.pos.backend.repository.SaleItemRepository;

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
}

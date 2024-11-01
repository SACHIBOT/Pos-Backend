package com.pos.backend.service;

import com.pos.backend.entity.Sale;
import com.pos.backend.entity.SaleItem;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface SaleService {

    Sale createSale(Sale sale);

    SaleItem addSaleItem(SaleItem saleItem) throws Exception;

    Sale getSaleById(Long saleId);

    void removeSaleItem(Long saleItemId);

    List<Sale> getAllSales();
}

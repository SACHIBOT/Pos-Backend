package com.pos.backend.service;

import com.pos.backend.entity.Sale;
import com.pos.backend.entity.SaleItem;
import org.springframework.stereotype.Service;

@Service
public interface SaleService {

    Sale createSale(Sale sale);

    SaleItem addSaleItem(SaleItem saleItem) throws Exception;

    Sale getSaleById(Long saleId);
}

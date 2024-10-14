package com.pos.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pos.backend.entity.Stock;

@Service
public interface StockService {

    Stock createStock(Stock stock);

    List<Stock> getAllStocks();

    Stock getStockById(Long id);

    Stock getStockByItemId(Long itemId);

    Stock updateStock(Long id, Stock stock);

    void deleteStock(Long id);

}
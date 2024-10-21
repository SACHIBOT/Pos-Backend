package com.pos.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pos.backend.entity.Stock;
import com.pos.backend.repository.StockRepository;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockRepository stockRepository;

    @Override
    public Stock createStock(Stock stock) {
        return stockRepository.save(stock);
    }

    @Override
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    @Override
    public Stock getStockById(Long id) {
        return stockRepository.findById(id).orElse(null);
    }

    @Override
    public Stock getStockByItemId(Long itemId) {
        return stockRepository.findByItemItemId(itemId);
    }

    @Override
    public Stock updateStock(Long id, Stock stock) {
        Stock existingStock = stockRepository.findById(id).orElse(null);

        if (existingStock == null) {
            return null;
        } else {
            existingStock.setItem(stock.getItem());
            existingStock.setQuantity(stock.getQuantity());
            existingStock.setStockId(stock.getStockId());
            return stockRepository.save(existingStock);

        }
    }

    @Override
    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }

}

package com.pos.backend.service;

import com.pos.backend.entity.Sale;
import com.pos.backend.entity.SaleItem;
import com.pos.backend.entity.Stock;
import com.pos.backend.repository.SaleItemRepository;
import com.pos.backend.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaleServiceImpl implements SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SaleItemRepository saleItemRepository;

    @Autowired
    private StockService stockService;

    @Override
    public Sale createSale(Sale sale) {
        sale.setTotalAmount(0.0);
        return saleRepository.save(sale);
    }

    @Override
    @Transactional
    public SaleItem addSaleItem(SaleItem saleItem) throws Exception {
        Sale sale = saleItem.getSale();
        int qty = saleItem.getQuantity();
        double price = saleItem.getPrice();

        if (sale == null || qty == 0 || price == 0) {
            throw new Exception("Required feilds are empty in saleitem");
        }

        Stock stock = stockService.getStockByItemId(saleItem.getItem().getItemId());
        if (stock == null) {
            throw new Exception("Item stock not found");
        }
        int stockQty = stock.getQuantity();
        if (stockQty < qty) {
            throw new Exception("Not enough stock");
        }
        stock.setQuantity(stockQty - qty);
        stockService.updateStock(stock.getStockId(), stock);
        double newTotal = sale.getTotalAmount() + (price * qty);
        sale.setTotalAmount(newTotal);

        sale = saleRepository.save(sale);
        saleItem.setSale(sale);
        return saleItemRepository.save(saleItem);
    }

    @Override
    public Sale getSaleById(Long saleId) {
        return saleRepository.findById(saleId).orElse(null);
    }

    @Override
    @Transactional
    public void removeSaleItem(Long saleItemId) {
        SaleItem saleItem = saleItemRepository.findById(saleItemId).orElse(null);
        if (saleItem != null) {

            Sale sale = saleItem.getSale();
            double itemTotal = saleItem.getPrice() * saleItem.getQuantity();
            double newTotal = sale.getTotalAmount() - itemTotal;

            sale.setTotalAmount(newTotal);
            saleRepository.save(sale);
            saleItemRepository.delete(saleItem);
        }
    }
}

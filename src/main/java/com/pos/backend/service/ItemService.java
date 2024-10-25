package com.pos.backend.service;

import org.springframework.stereotype.Service;

import java.util.List;

import com.pos.backend.entity.Item;

@Service
public interface ItemService {

    Item createItem(Item item);

    List<Item> getAllItems();

    Item getItemById(Long id);

    List<Item> getItemsByCategoryId(Long id);

    Item getItemByName(String itemName);

    Item updateItem(Item item);

    void deleteItem(Long id);

}

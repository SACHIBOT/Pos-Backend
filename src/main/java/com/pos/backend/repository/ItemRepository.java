package com.pos.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pos.backend.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findByItemName(String itemName);
}
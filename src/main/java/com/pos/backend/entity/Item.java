package com.pos.backend.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "items")
@JsonIgnoreProperties({ "saleItems" })
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(unique = true)
    private String itemName;
    private String description;
    private double price;

    @ManyToOne
    @JoinColumn(name = "item_category_id")
    private Category itemCategory;

    @OneToOne(mappedBy = "item")
    private Stock stock;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "sale_items", joinColumns = @JoinColumn(name = "item_id"), inverseJoinColumns = @JoinColumn(name = "sale_id"))
    private List<Sale> sales;

}

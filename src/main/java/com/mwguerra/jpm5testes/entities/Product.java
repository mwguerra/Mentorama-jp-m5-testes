package com.mwguerra.jpm5testes.entities;

public class Product extends Entity {
  public String name;
  public Double price;
  public Double maximumDiscount;
  public Integer quantityInStock;

  public Product(String name, Double price, Double maximumDiscount, Integer quantityInStock) {
    this.name = name;
    this.price = (price == null || price < 0) ? 0.0 : price;
    this.maximumDiscount = (maximumDiscount == null || maximumDiscount < 0) ? 0.0 : maximumDiscount;
    this.quantityInStock = (quantityInStock == null || quantityInStock < 0) ? 0 : quantityInStock;
  }
}

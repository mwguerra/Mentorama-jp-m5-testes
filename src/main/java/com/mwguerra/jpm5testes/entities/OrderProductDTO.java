package com.mwguerra.jpm5testes.entities;

import java.util.UUID;

public class OrderProductDTO extends Entity {
  public UUID id;
  public String name;
  public Double price;
  public Double discount;
  public Integer quantity;

  public OrderProductDTO(Product product, Double discount, Integer quantity) {
    this.id = product.getId();
    this.name = product.name;
    this.price = product.price;
    this.discount = (discount == null || discount < 0) ? 0.0 : discount;
    this.quantity = (quantity == null || quantity < 0) ? 0 : quantity;
  }
}

package com.mwguerra.jpm5testes.entities;

import java.util.UUID;

public class OrderProductControllerDTO extends Entity {
  public UUID id;
  public Double discount;
  public Integer quantity;

  public OrderProductControllerDTO(UUID id, Double discount, Integer quantity) {
    this.id = id;
    this.discount = discount;
    this.quantity = quantity;
  }
}

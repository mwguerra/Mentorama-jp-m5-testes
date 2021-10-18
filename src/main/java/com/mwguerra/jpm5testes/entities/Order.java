package com.mwguerra.jpm5testes.entities;

import java.util.Date;
import java.util.List;

public class Order extends Entity {
  private List<OrderProductDTO> products;
  private Date placedAt;
  private Double total;

  public Order(List<OrderProductDTO> products) {
    this.products = products;
    this.placedAt = new Date();
    this.total = 0.0;
    products.forEach(product -> this.total += product.price * product.quantity * (1 - product.discount));
  }

  public List<OrderProductDTO> getProducts() {
    return products;
  }

  public void setProducts(List<OrderProductDTO> products) {
    this.products = products;
    this.total = 0.0;
    products.forEach(product -> this.total += product.price * product.quantity * (1 - product.discount));
  }

  public Date getPlacedAt() {
    return placedAt;
  }

  public Double getTotal() {
    return total;
  }
}

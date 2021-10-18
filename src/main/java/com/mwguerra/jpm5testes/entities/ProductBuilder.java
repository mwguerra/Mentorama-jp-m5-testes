package com.mwguerra.jpm5testes.entities;

public class ProductBuilder extends Entity {
  private String name;
  private Double price;
  private Double maximumDiscount;
  private Integer quantityInStock;

  public ProductBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public ProductBuilder withPrice(Double price) {
    this.price = price;
    return this;
  }

  public ProductBuilder withMaximumDiscount(Double maximumDiscount) {
    this.maximumDiscount = maximumDiscount;
    return this;
  }

  public ProductBuilder withQuantityInStock(Integer quantityInStock) {
    this.quantityInStock = quantityInStock;
    return this;
  }

  public Product build() {
    return new Product(this.name, this.price, this.maximumDiscount, this.quantityInStock);
  }
}

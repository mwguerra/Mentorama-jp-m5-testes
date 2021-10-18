package com.mwguerra.jpm5testes.http.exceptions;

public class ProductNotFoundException extends RuntimeException {
  public ProductNotFoundException() {
    super("Product not found");
  }
}

package com.mwguerra.jpm5testes.repositories;

import com.mwguerra.jpm5testes.database.Database;
import com.mwguerra.jpm5testes.entities.Product;
import com.mwguerra.jpm5testes.http.exceptions.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Repository
public class ProductRepository {
  @Autowired
  private Database database;

  public List<Product> all() {
    return database.products.all();
  }

  public Product create(Product product) {
    return database.products.create(product);
  }

  public Product find(UUID id) {
    return database.products.find(id);
  }

  public void update(UUID id, Product product) {
    Product response = database.products.find(id);

    if (response == null) {
      throw new ProductNotFoundException();
    }

    database.products.update(id, product);
  }

  public void delete(UUID id) {
    Product response = database.products.delete(id);

    if (response == null) {
      throw new ProductNotFoundException();
    }
  }

  public void addToStock(UUID id, Integer quantity) {
    Product product = database.products.find(id);

    if (product == null) {
      throw new ProductNotFoundException();
    }

    product.quantityInStock += quantity;

    database.products.update(id, product);
  }
}

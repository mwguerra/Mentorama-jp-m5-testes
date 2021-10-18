package com.mwguerra.jpm5testes.services;

import com.mwguerra.jpm5testes.entities.Product;
import com.mwguerra.jpm5testes.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
  @Autowired
  private ProductRepository productRepository;

  public List<Product> index() {
    return productRepository.all();
  }

  public Product create(Product product) {
    return productRepository.create(product);
  }

  public Product show(UUID id) {
    return productRepository.find(id);
  }

  public void update(UUID id, Product product) {
    product.setId(id);

    productRepository.update(id, product);
  }

  public void delete(UUID id) {
    productRepository.delete(id);
  }

  public void addToStock(UUID id, Integer quantity) {
    productRepository.addToStock(id, quantity);
  }
}

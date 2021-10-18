package com.mwguerra.jpm5testes.http.controllers;

import com.mwguerra.jpm5testes.entities.Product;
import com.mwguerra.jpm5testes.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {
  @Autowired
  private ProductService productService;

  @GetMapping
  public ResponseEntity<List<Product>> index() {
    List<Product> products = productService.index();

    return new ResponseEntity<>(products, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<Product> create(@RequestBody Product product) {
    Product newStudent = productService.create(product);

    return new ResponseEntity<>(newStudent, HttpStatus.CREATED);
  }

  @PostMapping("/{id}")
  public ResponseEntity<Product> show(@PathVariable("id") UUID id) {
    Product product = productService.show(id);

    return new ResponseEntity<>(product, HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity update(@PathVariable("id") UUID id, @RequestBody Product product) {
    productService.update(id, product);

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity delete(@PathVariable("id") UUID id) {
    productService.delete(id);

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @PostMapping("/{id}/add-to-stock/{quantity}")
  public ResponseEntity<Product> addToStock(@PathVariable("id") UUID id, @PathVariable("quantity") Integer quantity) {
    productService.addToStock(id, quantity);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}

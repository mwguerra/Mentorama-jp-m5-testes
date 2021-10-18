package com.mwguerra.jpm5testes.http.controllers;

import com.mwguerra.jpm5testes.entities.Order;
import com.mwguerra.jpm5testes.entities.OrderProductControllerDTO;
import com.mwguerra.jpm5testes.entities.OrderProductDTO;
import com.mwguerra.jpm5testes.entities.Product;
import com.mwguerra.jpm5testes.services.OrderService;
import com.mwguerra.jpm5testes.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RequestMapping("/orders")
@RestController
public class OrderController {
  @Autowired
  private OrderService orderService;

  @Autowired
  private ProductService productService;

  @GetMapping
  public ResponseEntity<List<Order>> index() {
    List<Order> orders = orderService.index();

    return new ResponseEntity<>(orders, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<Order> create(@RequestBody List<OrderProductControllerDTO> products) {
    List<OrderProductDTO> orderProductDTOs = new ArrayList<>();

    products.forEach(p -> {
      Product product = productService.show(p.id);

      if (product != null) {
        orderProductDTOs.add(new OrderProductDTO(product, p.discount, p.quantity));
      }
    });

    if (orderProductDTOs.isEmpty()) {
      new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    Order newOrder = orderService.create(new Order(orderProductDTOs));

    return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
  }

  @PostMapping("/{id}")
  public ResponseEntity<Order> show(@PathVariable("id") UUID id) {
    Order order = orderService.show(id);

    return new ResponseEntity<>(order, HttpStatus.OK);
  }
}

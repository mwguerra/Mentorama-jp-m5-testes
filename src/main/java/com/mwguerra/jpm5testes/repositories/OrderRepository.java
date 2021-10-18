package com.mwguerra.jpm5testes.repositories;

import com.mwguerra.jpm5testes.database.Database;
import com.mwguerra.jpm5testes.entities.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class OrderRepository {
  @Autowired
  private Database database;

  public List<Order> all() {
    return database.orders.all();
  }

  public Order create(Order order) {
    return database.orders.create(order);
  }

  public Order find(UUID id) {
    return database.orders.find(id);
  }
}

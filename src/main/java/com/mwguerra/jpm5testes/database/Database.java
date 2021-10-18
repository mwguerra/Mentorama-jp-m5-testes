package com.mwguerra.jpm5testes.database;

import com.mwguerra.jpm5testes.database.tables.OrdersTable;
import com.mwguerra.jpm5testes.database.tables.ProductsTable;
import org.springframework.stereotype.Component;

@Component
public class Database {
  public ProductsTable products = new ProductsTable();
  public OrdersTable orders = new OrdersTable();

  public void seeder() {
  }
}

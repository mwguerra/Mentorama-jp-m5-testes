package com.mwguerra.jpm5testes.unit;

import com.mwguerra.jpm5testes.entities.Order;
import com.mwguerra.jpm5testes.entities.OrderProductDTO;
import com.mwguerra.jpm5testes.entities.Product;
import com.mwguerra.jpm5testes.services.OrderService;
import com.mwguerra.jpm5testes.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OrderTests {

  @Autowired
  private ProductService productService;

  @Autowired
  private OrderService orderService;

  Product product1;
  Product product2;
  List<OrderProductDTO> productsToOrder;

  @BeforeEach
  void setup() {
    productsToOrder = new ArrayList<>();

    product1 = new Product("Mobile Phone", 199.99, 0.1, 100);
    product2 = new Product("Spacecraft Helmet", 200.01, 0.2, 30);

    productService.create(product1);
    productService.create(product2);
  }

  @Test
  void itShouldAllowNewOrdersToBeCreated() {
    productsToOrder.add(new OrderProductDTO(product1, 0.05, 5)); // 949.9525
    productsToOrder.add(new OrderProductDTO(product2, 0.1, 10)); // 1800.09
    // Total: 2750.0425

    Order order = orderService.create(
      new Order(productsToOrder)
    );

    assertNotNull(order);
    assertEquals(1, orderService.index().size());
    assertEquals(2, orderService.show(order.getId()).getProducts().size());
    assertEquals(
      new Date().toInstant().truncatedTo(ChronoUnit.DAYS),
      orderService.show(order.getId()).getPlacedAt().toInstant().truncatedTo(ChronoUnit.DAYS)
    );
    assertEquals(2750.0425, order.getTotal());
  }

  @Test
  void itShouldLimitDiscountsToTheMaximumRegisteredForEachProduct() {
    productsToOrder.add(new OrderProductDTO(product1, 0.05, 5)); // 949.9525
    productsToOrder.add(new OrderProductDTO(product2, 10.9, 10)); // 1600.08
    // Total: 2550.0325

    Order order = orderService.create(
      new Order(productsToOrder)
    );

    assertNotNull(order);
    assertEquals(2, order.getProducts().size());
    assertEquals(2550.0325, order.getTotal());
  }

  @Test
  void itShouldLimitOrdersToTheMaximumQuantityInStockForEachProduct() {
    productsToOrder.add(new OrderProductDTO(product1, 0.0, 235)); // inStock: 100
    productsToOrder.add(new OrderProductDTO(product2, 0.0, 11));

    Order order = orderService.create(
      new Order(productsToOrder)
    );

    assertNotNull(order);
    assertEquals(111, order.getProducts()
      .stream()
      .reduce(0, (acc, product) -> acc + product.quantity, Integer::sum)
    );
  }
}

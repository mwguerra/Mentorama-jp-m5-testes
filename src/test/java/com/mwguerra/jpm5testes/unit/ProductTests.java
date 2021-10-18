package com.mwguerra.jpm5testes.unit;

import com.mwguerra.jpm5testes.entities.Product;
import com.mwguerra.jpm5testes.entities.ProductBuilder;
import static org.junit.jupiter.api.Assertions.*;

import com.mwguerra.jpm5testes.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductTests {

  @Autowired
  private ProductService productService;

  @Test
  void itShouldAllowNewProductsToBeCreated() {
    Product product = new ProductBuilder()
      .withName("Mobile Phone")
      .withPrice(199.99)
      .withMaximumDiscount(0.25)
      .withQuantityInStock(150)
      .build();

    assertEquals(0, productService.index().size());

    productService.create(product);

    assertEquals(1, productService.index().size());

    Product productFromDatabase = productService.show(product.getId());

    assertNotNull(productFromDatabase);
    assertEquals("Mobile Phone", productFromDatabase.name);
    assertEquals(199.99, productFromDatabase.price);
    assertEquals(0.25, productFromDatabase.maximumDiscount);
    assertEquals(150, productFromDatabase.quantityInStock);
  }

  @Test
  void itShouldNotAllowANegativeQuantityInStock() {
    Product product = new ProductBuilder()
      .withName("Mobile Phone")
      .withQuantityInStock(-10)
      .build();

    assertNotNull(product);
    assertEquals(0, product.quantityInStock);
  }

  @Test
  void itShouldNotAllowANegativeMaximumDiscount() {
    Product product = new ProductBuilder()
      .withName("Mobile Phone")
      .withMaximumDiscount(-0.5)
      .build();

    assertNotNull(product);
    assertEquals(0.0, product.maximumDiscount);
  }

  @Test
  void itShouldNotAllowANegativePrice() {
    Product product = new ProductBuilder()
      .withName("Mobile Phone")
      .withPrice(-50.00)
      .build();

    assertNotNull(product);
    assertEquals(0.0, product.price);
  }

  @Test
  void itShouldAllowProductsToBeUpdated() {
    Product product = new Product("Mobile Phone", 199.99, 0.1, 100);

    productService.create(product);

    product.quantityInStock = 350;
    productService.update(product.getId(), product);

    assertEquals(1, productService.index().size());
    assertEquals(350, productService.show(product.getId()).quantityInStock);
  }

  @Test
  void itShouldAllowProductsToBeDeleted() {
    Product product = new Product("Mobile Phone", 199.99, 0.1, 100);

    assertEquals(0, productService.index().size());

    productService.create(product);

    assertEquals(1, productService.index().size());

    productService.delete(product.getId());

    assertEquals(0, productService.index().size());
  }

}

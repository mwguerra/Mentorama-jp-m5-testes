package com.mwguerra.jpm5testes.services;

import com.mwguerra.jpm5testes.entities.Order;
import com.mwguerra.jpm5testes.entities.OrderProductDTO;
import com.mwguerra.jpm5testes.entities.Product;
import com.mwguerra.jpm5testes.http.exceptions.ProductNotFoundException;
import com.mwguerra.jpm5testes.repositories.OrderRepository;
import com.mwguerra.jpm5testes.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderService {
  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private ProductRepository productRepository;

  public List<Order> index() {
    return orderRepository.all();
  }

  public Order create(Order order) {
    handleBusinessRulesForOrder(order);

    return orderRepository.create(order);
  }

  private void handleBusinessRulesForOrder(Order order) {
    order.setProducts(
      order.getProducts()
        .stream()
        .map(this::handleBusinessRulesForProduct)
        .collect(Collectors.toList())
    );
  }

  private OrderProductDTO handleBusinessRulesForProduct(OrderProductDTO productDTO) {
    Product dbProduct = productRepository.find(productDTO.id);

    if (dbProduct == null) {
      throw new ProductNotFoundException();
    }

    Function<OrderProductDTO, OrderProductDTO> maximumDiscountRule = product -> {
      product.discount = product.discount > dbProduct.maximumDiscount ? dbProduct.maximumDiscount : product.discount;
      return product;
    };

    Function<OrderProductDTO, OrderProductDTO> notEnoughProductsInStockRule = product -> {
      product.quantity = product.quantity > dbProduct.quantityInStock ? dbProduct.quantityInStock : product.quantity;
      return product;
    };

    return maximumDiscountRule
      .andThen(notEnoughProductsInStockRule)
      .apply(productDTO);
  }

  public Order show(UUID id) {
    return orderRepository.find(id);
  }
}

package com.mwguerra.jpm5testes.integration;

import com.jayway.jsonpath.JsonPath;
import com.mwguerra.jpm5testes.entities.OrderProductDTO;
import com.mwguerra.jpm5testes.services.OrderService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OrderTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private OrderService orderService;

  @Test
  void itShouldShowAllOrders() throws Exception {
    String product1UUID = createProduct("Product 1", "599.00", "0.05", "10");
    String product2UUID = createProduct("Product 2", "120.00", "0.1", "25");

    JSONArray productsJsonArray = new JSONArray();
    productsJsonArray.put(new JSONObject()
      .put("id", product1UUID)
      .put("discount", "0.0")
      .put("quantity", "3")
    );
    productsJsonArray.put(new JSONObject()
      .put("id", product2UUID)
      .put("discount", "0.05")
      .put("quantity", "10")
    );

    this.mockMvc
      .perform(
        post("/orders")
          .content(productsJsonArray.toString())
          .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
      )
      .andDo(print())
      .andExpect(status().isCreated());


    this.mockMvc
      .perform(get("/orders"))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(1)))
      .andExpect(jsonPath("$[0].products", hasSize(2)));
  }

  @Test
  void itShouldCreateOneOrder() throws Exception {
    String product1UUID = createProduct("Product 1", "599.00", "0.05", "10");
    String product2UUID = createProduct("Product 2", "120.00", "0.1", "25");

    JSONArray productsJsonArray = new JSONArray();
    productsJsonArray.put(new JSONObject()
      .put("id", product1UUID)
      .put("discount", "0.0")
      .put("quantity", "3")
    );  // 1797
    productsJsonArray.put(new JSONObject()
      .put("id", product2UUID)
      .put("discount", "0.05")
      .put("quantity", "10")
    );  // 1140
    // Total: 2937

    this.mockMvc
      .perform(
        post("/orders")
          .content(productsJsonArray.toString())
          .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
      )
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.total", is(2937.0)));
  }

  @Test
  void itShouldShowAnOrder() throws Exception {
    String productUUID = createProduct("Product 1", "599.00", "0.05", "10");

    JSONObject jsonContent = new JSONObject();
    jsonContent.put("products", new JSONArray()
      .put(new JSONObject()
        .put("id", productUUID)
        .put("discount", "0.0")
        .put("quantity", "3")
      )
    );

    this.mockMvc
      .perform(
        post("/orders/" + productUUID)
          .content(jsonContent.toString())
          .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
      )
      .andDo(print())
      .andExpect(status().isOk());
  }

  private String createProduct(String name, String price, String maximumDiscount, String quantityInStock) throws Exception {
    JSONObject jsonContent = new JSONObject();
    jsonContent.put("name", name);
    jsonContent.put("price", price);
    jsonContent.put("maximumDiscount", maximumDiscount);
    jsonContent.put("quantityInStock", quantityInStock);

    String response = this.mockMvc
      .perform(
        post("/products")
          .content(jsonContent.toString())
          .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
      )
      .andReturn()
      .getResponse()
      .getContentAsString();

    return JsonPath.parse(response).read("$.id");
  }
}

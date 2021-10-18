package com.mwguerra.jpm5testes.integration;

import com.jayway.jsonpath.JsonPath;
import com.mwguerra.jpm5testes.services.ProductService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ProductService productService;

  @Test
  void itShouldShowAllProducts() throws Exception {
    createProduct("Product 1", "599.00", "0.05", "10");
    createProduct("Product 2", "120.00", "0.1", "25");

    this.mockMvc
      .perform(get("/products"))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(2)));
  }

  @Test
  void itShouldCreateOneProduct() throws Exception {
    createProduct("Product 1", "599.00", "0.05", "10")
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id", hasLength(36)));  // UUID
  }

  @Test
  void itShouldDeleteAProduct() throws Exception {
    String productResponse = createProduct("Product 1", "599.00", "0.05", "10")
      .andReturn()
      .getResponse()
      .getContentAsString();
    String productUUID = JsonPath.parse(productResponse).read("$.id");

    this.mockMvc
      .perform(delete("/products/" + productUUID))
      .andDo(print())
      .andExpect(status().isNoContent());

    this.mockMvc
      .perform(post("/products" + productUUID))
      .andDo(print())
      .andExpect(status().isNotFound());
  }

  @Test
  void itShouldShowAProduct() throws Exception {
    String productResponse = createProduct("Product 1", "599.00", "0.05", "10")
      .andReturn()
      .getResponse()
      .getContentAsString();
    String productUUID = JsonPath.parse(productResponse).read("$.id");

    this.mockMvc
      .perform(post("/products/" + productUUID))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(productUUID)))
      .andExpect(jsonPath("$.name", is("Product 1")));
  }

  @Test
  void itShouldShowAnErrorIfDeletingAProductWithAnInvalidUUIDCode() throws Exception {
    this.mockMvc
      .perform(delete("/products/12341234-1234-1234-1234-123412341234"))
      .andDo(print())
      .andExpect(status().isBadRequest());
  }

  @Test
  void itShouldShowAnErrorIfUpdatingAProductWithAnInvalidUUIDCode() throws Exception {
    this.mockMvc
      .perform(put("/products/12341234-1234-1234-1234-123412341234"))
      .andDo(print())
      .andExpect(status().isBadRequest());
  }

  @Test
  void itShouldUpdateAProduct() throws Exception {
    String productResponse = createProduct("Product 1", "599.00", "0.05", "10")
      .andReturn()
      .getResponse()
      .getContentAsString();
    String productUUID = JsonPath.parse(productResponse).read("$.id");

    JSONObject jsonContent = new JSONObject();
    jsonContent.put("name", "Product 1 - UPDATED");
    jsonContent.put("price", "599.00");
    jsonContent.put("maximumDiscount", "0.05");
    jsonContent.put("quantityInStock", "10");

    this.mockMvc
      .perform(
        put("/products/" + productUUID)
          .content(jsonContent.toString())
          .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
      );

    this.mockMvc
      .perform(post("/products/" + productUUID))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(productUUID)))
      .andExpect(jsonPath("$.name", is("Product 1 - UPDATED")));
  }

  @Test
  void itShouldIncreaseTheStockForAProduct() throws Exception {
    String productResponse = createProduct("Product 1", "599.00", "0.05", "10")
      .andReturn()
      .getResponse()
      .getContentAsString();
    String productUUID = JsonPath.parse(productResponse).read("$.id");

    this.mockMvc
      .perform(
        post("/products/" + productUUID + "/add-to-stock/" + 15)
          .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isNoContent());

    this.mockMvc
      .perform(post("/products/" + productUUID))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.quantityInStock", is(25)));
  }

  private ResultActions createProduct(String name, String price, String maximumDiscount, String quantityInStock) throws Exception {
    JSONObject jsonContent = new JSONObject();
    jsonContent.put("name", name);
    jsonContent.put("price", price);
    jsonContent.put("maximumDiscount", maximumDiscount);
    jsonContent.put("quantityInStock", quantityInStock);

    return this.mockMvc
      .perform(
        post("/products")
          .content(jsonContent.toString())
          .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
      );
  }
}

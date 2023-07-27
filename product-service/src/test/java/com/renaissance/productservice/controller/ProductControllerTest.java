package com.renaissance.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renaissance.productservice.dto.ProductRequest;
import com.renaissance.productservice.repository.ProductRepository;
import com.renaissance.productservice.model.Product;
import com.renaissance.productservice.service.ProductServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // deactivate the default in built H2 in memory database
@AutoConfigureMockMvc
public class ProductControllerTest {
    @Container //Create the test container with database settings
    static MySQLContainer sqlContainer = new MySQLContainer(DockerImageName.parse("mysql:8.0.30"))
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", sqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", sqlContainer::getUsername);
        registry.add("spring.datasource.password", sqlContainer::getPassword);
    }
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductServiceImpl productService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldCreateProducts() throws Exception {
        ProductRequest productRequest = getProductRequest();
        //convert ProductRequest obj to string
        String productRequestString = objectMapper.writeValueAsString(productRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestString))
                        .andExpect(status().isCreated());

        Assertions.assertNotNull(productRepository.findAll());
        Assertions.assertEquals(1, productRepository.findAll().size());
    }
    @Test
    public void shouldReturnAllProducts() throws Exception {
        Product product = getProduct();
       // productRepository.save(product);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/product/products")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
        Assertions.assertEquals(1, productRepository.findAll().size());
    }
    @Test
    public void shouldReturnProductById() throws Exception {
        Product product = getProduct();
        // productRepository.save(product);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/product/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Assertions.assertEquals(true, productRepository.findById(1).isPresent());
    }
    private ProductRequest getProductRequest()  {
        return  ProductRequest.builder()
                .name("iphone")
                .description("iphone13")
                .price(BigDecimal.valueOf(30000))
                .build();
    }
    private Product getProduct()  {
        return  Product.builder()
                .name("iphone")
                .description("iphone13")
                .price(BigDecimal.valueOf(30000))
                .build();
    }
}

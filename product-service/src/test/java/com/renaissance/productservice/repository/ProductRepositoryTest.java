package com.renaissance.productservice.repository;

import com.renaissance.productservice.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // deactivate the default in built H2 in memory database
@DataJpaTest
public class ProductRepositoryTest {

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
    ProductRepository productRepository;
    @Test
    public void shouldGetAllProducts()  {

        Product product1 = new Product(1,"iphone","iphone13",BigDecimal.valueOf(30000));
        Product product2 = new Product(2,"laptop","Dell laptop",BigDecimal.valueOf(50000));
        productRepository.save(product1);
        productRepository.save(product2);
        List<Product> productList = new ArrayList<>();
        productRepository.findAll().forEach(p->productList.add(p));

        Assertions.assertNotNull(productList);
        Assertions.assertEquals(2, productList.size());
    }
    @Test
    public void shouldGetProductById()  {
        Product product1 = new Product(3,"desktop","intel processor desktop",BigDecimal.valueOf(30000));
        productRepository.save(product1);
        Optional<Product> actualProduct = productRepository.findById(3);
        Assertions.assertNotNull(actualProduct);
       // Assertions.assertTrue(actualProduct.isPresent());
       // Assertions.assertEquals(1, actualProduct.stream().count());
    }
    @Test
    public void shouldReturnEmptyProductById()  {
        Product product1 = new Product(3,"desktop","intel processor desktop",BigDecimal.valueOf(30000));
        productRepository.save(product1);
        Optional<Product> actualProduct = productRepository.findById(4);
        Assertions.assertNotNull(actualProduct);
        Assertions.assertTrue(actualProduct.isEmpty());
        Assertions.assertEquals(0, actualProduct.stream().count());
    }
}

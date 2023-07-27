package com.renaissance.productservice.service;

import com.renaissance.productservice.dto.ProductRequest;
import com.renaissance.productservice.dto.ProductResponse;
import com.renaissance.productservice.exception.CommonException;
import com.renaissance.productservice.repository.ProductRepository;
import com.renaissance.productservice.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    ProductRepository productRepository;
    @InjectMocks
    ProductServiceImpl productService;

    @Test
    public void shouldReturnExceptionIfProductRequestIsNull() throws Exception    {
        CommonException exception = Assertions.assertThrows(
                CommonException.class,
                ()->productService.addProduct(null));
        Assertions.assertEquals("Product request cannot be null", exception.getMessage());
    }
    @Test
    public void shouldAddProduct() throws Exception    {
        ProductRequest productRequest = new ProductRequest(
                "laptop",
                "Dell laptop",
                BigDecimal.valueOf(40000)
        );
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
         when(productRepository.save(product)).thenReturn(product);
         productService.addProduct(productRequest);
         Assertions.assertEquals("laptop",product.getName());
        Assertions.assertEquals("Dell laptop",product.getDescription());
        Assertions.assertEquals(BigDecimal.valueOf(40000),product.getPrice());
    }
    @Test
    public void shouldReturnEmptyListIfProductTableIsEmpty() throws Exception  {
        List<ProductResponse> productResponseList = new ArrayList<>();
        List<Product> productList = new ArrayList<>();
        when(productRepository.findAll()).thenReturn(productList);
        List<ProductResponse>  actualProductResponseList = productService.getAllProducts();
        Assertions.assertNotNull(actualProductResponseList);
        Assertions.assertEquals(productResponseList, actualProductResponseList);
        Assertions.assertEquals(productResponseList.isEmpty(), actualProductResponseList.isEmpty());
        Assertions.assertEquals(productResponseList.size(), actualProductResponseList.size());
    }
    @Test
    public void shouldReturnProductList() throws Exception {
        List<Product> productList = new ArrayList<>(Arrays.asList(
                new Product(1,"iphone","iphone13",BigDecimal.valueOf(30000)),
                new Product(2,"laptop","Dell laptop",BigDecimal.valueOf(50000))
        ));
        List<ProductResponse> productResponseList = new ArrayList<>(Arrays.asList(
                new ProductResponse(1,"iphone","iphone13",BigDecimal.valueOf(30000)),
                new ProductResponse(2,"laptop","Dell laptop",BigDecimal.valueOf(50000))
        ));

        when(productRepository.findAll()).thenReturn(productList);
        List<ProductResponse> actualproductResponseList = productService.getAllProducts();
        Assertions.assertNotNull(actualproductResponseList);
        Assertions.assertEquals(productResponseList, actualproductResponseList);
        Assertions.assertEquals(productResponseList.size(), actualproductResponseList.size());
    }
    @Test
    public void shouldReturnEmptyProductIfInvalidProductId()    {
        Product product1 = new Product(3,"desktop"
                ,"intel processor desktop"
                ,BigDecimal.valueOf(30000));
        when(productRepository.findById(4)).thenReturn(Optional.of(product1));
        ProductResponse actualProduct = productService.getProductById(4);
        Assertions.assertNotNull(actualProduct);
        Assertions.assertNotEquals(product1, actualProduct);
    }
    @Test
    public void shouldReturnProductByProductId()    {
        Product product1 = new Product(3,"desktop","intel processor desktop",BigDecimal.valueOf(30000));
        when(productRepository.findById(3)).thenReturn(Optional.of(product1));
        ProductResponse actualProduct = productService.getProductById(3);
        Assertions.assertNotNull(actualProduct);
        Assertions.assertEquals(product1.getId(), actualProduct.getId());
        Assertions.assertEquals(product1.getName(), actualProduct.getName());
        Assertions.assertEquals(product1.getDescription(), actualProduct.getDescription());
        Assertions.assertEquals(product1.getPrice(), actualProduct.getPrice());
    }

}

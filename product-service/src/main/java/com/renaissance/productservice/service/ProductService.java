package com.renaissance.productservice.service;

import com.renaissance.productservice.dto.ProductRequest;
import com.renaissance.productservice.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    void addProduct(ProductRequest productRequest);
    List<ProductResponse> getAllProducts();
    ProductResponse getProductById(int id);
}

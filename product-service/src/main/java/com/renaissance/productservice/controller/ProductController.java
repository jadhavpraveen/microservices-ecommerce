package com.renaissance.productservice.controller;

import com.renaissance.productservice.dto.ProductRequest;
import com.renaissance.productservice.dto.ProductResponse;
import com.renaissance.productservice.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;
    @PostMapping("products")
    public ResponseEntity<ProductRequest> addProduct(@RequestBody ProductRequest productRequest)  {
        productService.addProduct(productRequest);
        return new ResponseEntity<>(productRequest, HttpStatus.CREATED);
    }
    @GetMapping("products")
    public ResponseEntity<List<ProductResponse>> getAllProducts()   {
        List<ProductResponse> productResponseList = productService.getAllProducts();
        return new ResponseEntity<>(productResponseList, HttpStatus.OK);
    }
    @GetMapping("products/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable(value = "id") int id)   {
        ProductResponse productResponse = productService.getProductById(id);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }
}

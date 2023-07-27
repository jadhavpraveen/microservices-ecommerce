package com.renaissance.productservice.service;

import com.renaissance.productservice.dto.ProductRequest;
import com.renaissance.productservice.dto.ProductResponse;
import com.renaissance.productservice.exception.CommonException;
import com.renaissance.productservice.exception.ResourceNotFoundException;
import com.renaissance.productservice.repository.ProductRepository;
import com.renaissance.productservice.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public void addProduct(ProductRequest productRequest) {
        if (productRequest == null) throw new CommonException("Product request cannot be null");

        //Map Product obj with ProductRequest obj
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        //return products.stream().map(p->mapToProductResponse(p)).toList();
        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        //Map ProductResponse obj to Product obj
        ProductResponse productResponse = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
        return productResponse;
    }

    @Override
    public ProductResponse getProductById(int id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()) {
            log.info("No product exist for product id : " + id);
            throw new ResourceNotFoundException("No product exist for product id : " + id);
        }
        return mapToProductResponse(product.get());
    }

}

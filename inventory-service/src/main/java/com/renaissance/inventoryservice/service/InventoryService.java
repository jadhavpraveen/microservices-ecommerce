package com.renaissance.inventoryservice.service;

import com.renaissance.inventoryservice.dto.InventoryResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InventoryService {
   // List<InventoryResponse> isInStock(String skuCode);

    @Transactional(readOnly = true)
    abstract List<InventoryResponse> isInStock(List<String> skuCode);
}

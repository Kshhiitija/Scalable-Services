package com.example.inventory.service;

import com.example.inventory.model.Inventory;
import com.example.inventory.repository.InventoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepo inventoryRepo;

    public List<Inventory> getAllInventory() {
        return inventoryRepo.findAll();
    }

    public Inventory getProductStatus(String productId) {
        return inventoryRepo.findById(productId).orElse(null);
    }

    public Inventory addProduct(Inventory product) {
        Inventory productExists = inventoryRepo.findById(product.getProductId()).orElse(null);
        if (productExists != null) {
            // Product already exists in the inventory, return null
            return null;
        } else {
            // Product does not exist, so save it and return the saved product
            return inventoryRepo.save(product);
        }
    }

    public void deleteProduct(String productId) {
        inventoryRepo.deleteById(productId);
    }

    public Inventory updateProduct(String productId ,Map<String, Object> req) {
        Inventory product = inventoryRepo.findById(productId).orElse(null);

        if(product == null){
            return null;
        } else {
            String op = req.get("op").toString();
            Integer qty = Integer.valueOf(req.get("qty").toString());

            if(op.equals(Operation.SUB.toString())) {
                qty *= -1;
            }

            product.setQuantity(product.getQuantity() + qty);
            return inventoryRepo.save(product);
        }


    }
}

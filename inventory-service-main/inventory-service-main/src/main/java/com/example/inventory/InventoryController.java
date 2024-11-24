package com.example.inventory;

import com.example.inventory.model.Inventory;
import com.example.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("")
    public ResponseEntity<List<Inventory>> getAllInventory() {
        return new ResponseEntity<>(inventoryService.getAllInventory(), HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Inventory> getProductInventory(@PathVariable("productId") String productId) {
        return new ResponseEntity<>(inventoryService.getProductStatus(productId), HttpStatus.OK);
    }

    // Add new product detail to inventory db
    @PostMapping("")
    public ResponseEntity<?> addProduct(@RequestBody Inventory formData){
        Inventory product = inventoryService.addProduct(formData);
        if(product == null){
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "Product already exists!");
            responseBody.put("status", "error");
            return new ResponseEntity<>(responseBody, HttpStatus.ALREADY_REPORTED);
        }
        return new ResponseEntity<Inventory>(product, HttpStatus.CREATED);
    }

    // delete product detail from inventory db
    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable String productId){
        inventoryService.deleteProduct(productId);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable("productId") String productId, @RequestBody Map<String, Object> req) {
        return new ResponseEntity<>(inventoryService.updateProduct(productId, req), HttpStatus.OK);
    }

}

package com.example.productservice.controller.Seller;

import com.example.productservice.Entity.Product;
import com.example.productservice.Product.ProductRequest;
import com.example.productservice.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products/seller")
public class ProductControllerSeller {
    @Autowired
    private ProductService productService;

    // add
    @PostMapping("")
    public ResponseEntity<?> addProduct(@RequestBody ProductRequest product) {
        Product newProduct = productService.addProduct(product);
        if(newProduct != null){
            return new ResponseEntity<>("Insert Success", HttpStatus.CREATED);

        }else {
            return new ResponseEntity<>("Insert NOT Success", HttpStatus.BAD_REQUEST);
        }
    }

    // update

    @PutMapping("/{id}/basic")
    public ResponseEntity<String> updateProductBasic(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        try {
            Product updatedProduct = productService.updateProductBasic(id, productRequest);
            return new ResponseEntity<>("Product updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update product", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/detail")
    public ResponseEntity<String> updateProductDetail(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        try {
            Product updatedProduct = productService.updateProductDetail(id, productRequest);
            return new ResponseEntity<>("Product details updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update product details", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/sell")
    public ResponseEntity<String> updateProductSell(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        try {
            Product updatedProduct = productService.updateProductSell(id, productRequest);
            return new ResponseEntity<>("Product sell information updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update product sell information", HttpStatus.BAD_REQUEST);
        }
    }

        @DeleteMapping("/{id}/sizequantity")
    public ResponseEntity<String> deleteSizeQuantity(@PathVariable Long id) {

        productService.deleteSizeQuantityById(id);
        return ResponseEntity.ok("SizeQuantity deleted successfully");
    }

    @PutMapping("/{id}/ship")
    public ResponseEntity<String> updateProductShip(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        try {
            Product updatedProduct = productService.updateProductShip(id, productRequest);
            return new ResponseEntity<>("Product shipping information updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update product shipping information", HttpStatus.BAD_REQUEST);
        }
    }

    // hidden product
    @PatchMapping("/{id}/public")
    public ResponseEntity<Void> updateIsPublic(@PathVariable Long id, @RequestParam boolean isPublic) {
        productService.updateIsPublic(id, isPublic);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}

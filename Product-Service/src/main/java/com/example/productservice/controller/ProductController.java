package com.example.productservice.controller;

import com.example.productservice.Entity.Product;
import com.example.productservice.Reponse.*;
import com.example.productservice.Reponse.Order.OrderData;
import com.example.productservice.Reponse.Order.OrderDataRequest;
import com.example.productservice.Reponse.ReponseOrder.ReponseOrderData;
import com.example.productservice.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    ///api/v1/products/search
    @GetMapping("/test")
    public String testApi() {
        return "products Service API is working!";
    }

    @Autowired
    private ProductService productService;
    @PatchMapping("/{id}/increment-view")
    public ResponseEntity<Void> incrementProductView(@PathVariable Long id) {
        productService.incrementProductView(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductReponSingle> getProductById(@PathVariable Long id) {
        Product product = productService.getById(id);
        ProductReponSingle productReponSingle = new ProductReponSingle();
        productReponSingle.setMessage("Lấy sản phẩm thành công");
        productReponSingle.setData(product);
        return new ResponseEntity<>(productReponSingle, HttpStatus.OK);
    }


    @GetMapping("/{id}/ElasticSearch")
    public ResponseEntity<ProductReponSingle> getProductByIdE(@PathVariable Long id) {
        Product product = productService.getByIdE(id);
        ProductReponSingle productReponSingle = new ProductReponSingle();
        productReponSingle.setMessage("Lấy sản phẩm thành công");
        productReponSingle.setData(product);
        return new ResponseEntity<>(productReponSingle, HttpStatus.OK);
    }

    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<Product>> getProductsByShopId(@PathVariable Long shopId) {
        List<Product> products = productService.getListByShopId(shopId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


    // http://localhost:3000/?page=1&price_max=4444444&price_min=30000&rating_filter=2&category=8&name=2121
    @GetMapping("/search")
    public ResponseEntity<?>  searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) Double price_min,
            @RequestParam(required = false) Double price_max,
            @RequestParam(required = false, defaultValue = "price") String sort_by,
            @RequestParam(required = false, defaultValue = "asc") String order,
            @RequestParam(required = false) Integer rating_filter,
            @RequestParam(required = false) String deeplearning,
            @RequestParam(defaultValue = "0") int page, // Giá trị mặc định là trang 0
            @RequestParam(defaultValue = "10") int limit) { // Giá trị mặc định là kích thước trang 10

        Sort sort = Sort.by(Sort.Order.asc(sort_by));
        if ("desc".equalsIgnoreCase(order)) {
            sort = Sort.by(Sort.Order.desc(sort_by));
        }

        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Product> products=  productService.findAllWithFiltersAndSorting(deeplearning,name, category, price_min, price_max, sort_by, order, rating_filter, pageable);
        List<Product> list = products.stream().toList();
        Pagination pagination = new Pagination(pageable.getPageNumber(),0, pageable.getPageSize());
        ProductData productData = new ProductData(list,pagination);
        ProductReponse p = new ProductReponse("Thành Công", productData);
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    // http://localhost:3000/?page=1&price_max=4444444&price_min=30000&rating_filter=2&category=8&name=2121
    @GetMapping("/search-elasticsearch")
    public ResponseEntity<?>  searchProductsE(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) Double price_min,
            @RequestParam(required = false) Double price_max,
            @RequestParam(required = false, defaultValue = "price") String sort_by,
            @RequestParam(required = false, defaultValue = "asc") String order,
            @RequestParam(required = false) Integer rating_filter,
            @RequestParam(required = false) String deeplearning,
            @RequestParam(defaultValue = "0") int page, // Giá trị mặc định là trang 0
            @RequestParam(defaultValue = "10") int limit) { // Giá trị mặc định là kích thước trang 10

        Sort sort = Sort.by(Sort.Order.asc(sort_by));
        if ("desc".equalsIgnoreCase(order)) {
            sort = Sort.by(Sort.Order.desc(sort_by));
        }

        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Product> products=  productService.findAllWithFiltersAndSorting(deeplearning,name, category, price_min, price_max, sort_by, order, rating_filter, pageable);
        List<Product> list = products.stream().toList();
        Pagination pagination = new Pagination(pageable.getPageNumber(),0, pageable.getPageSize());
        ProductData productData = new ProductData(list,pagination);
        ProductReponse p = new ProductReponse("Thành Công", productData);
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @PostMapping("/by-ids")
    public ResponseEntity<List<Product>> getProductsByIds(@RequestBody List<Long> ids) {
        List<Product> products = productService.getProductsByIds(ids);
        return ResponseEntity.ok(products);
    }
    @GetMapping("/{idProduct}/size/{idSizeQuantity}")
    public ResponseEntity<?> getProductWithSizeQuantity(
            @PathVariable Long idProduct,
            @PathVariable Long idSizeQuantity) {

        Product response = productService.findProductWithSize(idProduct, idSizeQuantity);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }
    @PostMapping("/details")
    public ResponseEntity<List<Product_Promotion_SizeQuantityy_GET>> getOrderDetails(@RequestBody ReponseOrderData reponseOrderData) {
        List<Product_Promotion_SizeQuantityy_GET> orderDetails = productService.getOderDetails(reponseOrderData);
        return ResponseEntity.ok(orderDetails);
    }

    @GetMapping("/plus-view/{id}")
    public String plusView(@PathVariable Long id) {
        productService.plusView(id);
        return  "ok";
    }

    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody List<OrderDataRequest> orderDataRequests) {
        // Validate input (optional)
        if (orderDataRequests == null || orderDataRequests.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Call service to process the order data
        List<OrderData> orderDataList = productService.listOrderData(orderDataRequests);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDataList);
    }

}

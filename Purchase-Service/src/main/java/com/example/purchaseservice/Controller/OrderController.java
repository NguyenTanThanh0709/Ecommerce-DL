package com.example.purchaseservice.Controller;

import com.example.purchaseservice.Entity.Order;
import com.example.purchaseservice.Entity.OrderItem;
import com.example.purchaseservice.Request.OrderItemRequest;
import com.example.purchaseservice.Request.OrderRequest;
import com.example.purchaseservice.Service.OrderService;
import com.example.purchaseservice.Service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/purchases/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProducerService producerService;

    @PostMapping("/")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        Order createdOrder = orderService.createOrder(orderRequest);
        if(createdOrder != null){
            List<OrderItem> orderItems = createdOrder.getOrderItems();
            StringBuilder dataBuilder = new StringBuilder();

            for (OrderItem o : orderItems) {
                // Construct the format: productId-quantity-idSizeQuantity
                String itemData = o.getProductId() + "-" + o.getQuantity();

                // Include idSizeQuantity if it's not null
                if (o.getIdSizeQuantity() != null) {
                    itemData += "-" + o.getIdSizeQuantity();
                }else {
                    itemData += "-" + "0";
                }

                // Append the constructed item data to the main string
                dataBuilder.append(itemData).append("_");
            }

// Remove the last underscore if there are any items
            if (dataBuilder.length() > 0) {
                dataBuilder.setLength(dataBuilder.length() - 1);
            }

// Convert StringBuilder to String
            String data = dataBuilder.toString() + "@" + createdOrder.getId().toString();

// Output or return the constructed string
            System.out.println(data);

            producerService.sendMessage(data);


            return new ResponseEntity<>(createdOrder.getId().toString(), HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>("Not Success", HttpStatus.CREATED);

        }
    }

    // update status
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok("OK");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    // get by id
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // get order BY customerId
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getOrdersByCustomerId(@PathVariable Long customerId, @RequestParam String status) {
        List<Order> orders = orderService.getOrdersByCustomerId(customerId, status);
        return ResponseEntity.ok(orders);
    }

    // get order By shopId
    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<Order>> getOrdersByShopId(@PathVariable Long shopId, @RequestParam String status) {
        List<Order> orders = orderService.getOrdersByShopId(shopId, status);
        return ResponseEntity.ok(orders);
    }
    @GetMapping("/status/{status}")
    public List<Order> getOrdersByStatus(@PathVariable String status) {
        return orderService.getOrdersByStatus(status);
    }

}

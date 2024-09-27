package com.example.purchaseservice.Service;

import com.example.purchaseservice.Entity.Order;

import com.example.purchaseservice.Request.OrderRequest;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    public Order createOrder(OrderRequest orderRequest);
    Order updateOrderStatus(Long id, String status);
    Optional<Order> getOrderById(Long id);
    public List<Order> getOrdersByCustomerId(Long customerId, String status);
    public List<Order> getOrdersByShopId(Long shopId, String status);
    public List<Order> getOrdersByStatus(String status);
}

package com.example.purchaseservice.Service.impl;

import com.example.purchaseservice.Entity.Order;
import com.example.purchaseservice.Entity.Payment;
import com.example.purchaseservice.Repository.OrderRepository;
import com.example.purchaseservice.Repository.PaymentRepository;
import com.example.purchaseservice.Request.PaymentRequest;
import com.example.purchaseservice.Service.OrderService;
import com.example.purchaseservice.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class PaymentImple implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderService orderRepository;

    public Payment addPayment(PaymentRequest paymentRequest) {
        Optional<Order> order = orderRepository.getOrderById(paymentRequest.getOrderId());
        Order p = null;
        if(order.isPresent()){
            p = order.get();
        }
        Payment payment = Payment.builder()
                .order(p) // Giả định Order chỉ có id, nếu không, bạn cần lấy Order từ DB.
                .paymentDate(new Date())
                .status(paymentRequest.getStatus())
                .method(paymentRequest.getMethod())
                .amount(p.getTotalMoney())
                .build();
        return paymentRepository.save(payment);
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    public Optional<Payment> getPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    public Optional<Payment> updatePaymentStatus(Long id, String status) {
        Optional<Payment> payment = paymentRepository.findById(id);
        if (payment.isPresent()) {
            Payment updatedPayment = payment.get();
            updatedPayment.setStatus(status);
            paymentRepository.save(updatedPayment);
            return Optional.of(updatedPayment);
        }
        return Optional.empty();
    }
}

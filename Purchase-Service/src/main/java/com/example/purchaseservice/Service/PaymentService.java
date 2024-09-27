package com.example.purchaseservice.Service;

import com.example.purchaseservice.Entity.Payment;
import com.example.purchaseservice.Request.PaymentRequest;

import java.util.Optional;

public interface PaymentService {
    public Payment addPayment(PaymentRequest paymentRequest);
    public Optional<Payment> getPaymentById(Long id);
    public Optional<Payment> getPaymentByOrderId(Long orderId);
    public Optional<Payment> updatePaymentStatus(Long id, String status);
}

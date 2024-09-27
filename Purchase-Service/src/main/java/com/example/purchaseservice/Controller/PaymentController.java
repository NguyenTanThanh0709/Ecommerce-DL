package com.example.purchaseservice.Controller;

import com.example.purchaseservice.Entity.Payment;
import com.example.purchaseservice.Request.PaymentRequest;
import com.example.purchaseservice.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/purchases/payments")
public class PaymentController {

    @GetMapping("/test")
    public String testApi() {
        return "payment Service API is working!";
    }

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Payment> addPayment(@RequestBody PaymentRequest paymentRequest) {
        Payment payment = paymentService.addPayment(paymentRequest);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        Optional<Payment> payment = paymentService.getPaymentById(id);
        return payment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Payment> getPaymentByOrderId(@PathVariable Long orderId) {
        Optional<Payment> payment = paymentService.getPaymentByOrderId(orderId);
        return payment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.ok().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Payment> updatePaymentStatus(@PathVariable Long id, @RequestParam String status) {
        Optional<Payment> payment = paymentService.updatePaymentStatus(id, status);
        return payment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

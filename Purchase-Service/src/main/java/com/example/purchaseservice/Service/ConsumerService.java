package com.example.purchaseservice.Service;

import com.example.purchaseservice.Request.PaymentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class ConsumerService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;


    @KafkaListener(topics = "updatestatus", containerFactory = "kafkaListenerStringFactory", groupId = "tpd-loggers")
    public void consumeUpdateStatus(String id) {
        logger.info("**** -> Consumed message -> {}", id);
        Long idorder = Long.parseLong(id);
        orderService.updateOrderStatus(idorder, "cancelled" );
    }


    @KafkaListener(topics = "createpayment", containerFactory = "kafkaListenerStringFactory", groupId = "tpd-loggers")
    public void consumeCreatePayment(String id) {
        logger.info("**** -> Consumed Super Hero :: {}", id);
        Long idorder = Long.parseLong(id);
        PaymentRequest paymentRequest = new PaymentRequest(
                0l,idorder, null,"Đã thanh toán","MOMO", 0
        );
        paymentService.addPayment(paymentRequest);
    }

}
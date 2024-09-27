package com.example.purchaseservice.Service;


import com.example.purchaseservice.Request.OrderItemRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class ProducerService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String TOPIC = "updatequantityproduct";
    private static final String TOPIC1 = "updatequantityproduct1";
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {
        logger.info("#### -> Publishing message -> {}", message);
        kafkaTemplate.send(TOPIC, message);
    }

    public void sendMessage1(String message) {
        logger.info("#### -> Publishing message -> {}", message);
        kafkaTemplate.send(TOPIC1, message);
    }

}

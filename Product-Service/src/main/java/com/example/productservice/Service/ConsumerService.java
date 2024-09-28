package com.example.productservice.Service;

import com.example.productservice.Entity.Product;
import com.example.productservice.Entity.ProductReview;
import com.example.productservice.Product.ProductRequest;
import com.example.productservice.Service.ElasticSearch.ProductEService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class ConsumerService {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductEService productEService;

    private final Logger logger = LoggerFactory.getLogger(getClass());




    @KafkaListener(topics = "updatequantityproduct", containerFactory = "kafkaListenerStringFactory", groupId = "tpd-loggers1")
    public void consumeMessage(String message) {
        logger.info("**** -> Consumed message -> {}", message);
        productService.HandleQuantityProductAdd(message);
    }

    @KafkaListener(topics = "updatequantityproduct1", containerFactory = "kafkaListenerStringFactory", groupId = "tpd-loggers1")
    public void consumeMessage1(String message) {
        logger.info("**** -> Consumed message -> {}", message);
        productService.HandleQuantityProductSub(message);
    }

    // =================================================================================

    @KafkaListener(topics = "addProduct", containerFactory = "kafkaListenerJsonFactory1", groupId = "tpd-loggers1")
    public void consumeMessage01(Product product) {
        logger.info("**** -> Consumed message -> {}", product.toString());
        productEService.addProduct(product);
    }

    @KafkaListener(topics = "updateProductBasic", containerFactory = "kafkaListenerJsonFactory1", groupId = "tpd-loggers1")
    public void consumeMessage2(Product product) {
        logger.info("**** -> Consumed message -> {}", product.getId());
        productEService.updateProductBasic(product);
    }

    @KafkaListener(topics = "updateProductDetail", containerFactory = "kafkaListenerJsonFactory1", groupId = "tpd-loggers1")
    public void consumeMessage3(Product product) {
        logger.info("**** -> Consumed message -> {}", product.toString());
        productEService.updateProductDetail(product);
    }

    @KafkaListener(topics = "updateProductSell", containerFactory = "kafkaListenerJsonFactory1", groupId = "tpd-loggers1")
    public void consumeMessage4(Product product) {
        logger.info("**** -> Consumed message -> {}", product.toString());
        productEService.updateProductSell(product);
    }

    @KafkaListener(topics = "updateProductShip", containerFactory = "kafkaListenerJsonFactory1", groupId = "tpd-loggers1")
    public void consumeMessage5(Product product) {
        logger.info("**** -> Consumed message -> {}", product.toString());
        productEService.updateProductShip(product);
    }

    @KafkaListener(topics = "addReview", containerFactory = "kafkaListenerJsonFactory1", groupId = "tpd-loggers1")
    public void consumeMessage8(Product product) {
        logger.info("**** -> Consumed message -> {}", product.toString());
        productEService.addReview(product);
    }

    @KafkaListener(topics = "plusView", containerFactory = "kafkaListenerStringFactory", groupId = "tpd-loggers1")
    public void consumeMessage11(String message) {
        logger.info("**** -> Consumed message -> {}", message);
        productEService.plusView(message);
    }


    @KafkaListener(topics = "updateIsPublic", containerFactory = "kafkaListenerStringFactory", groupId = "tpd-loggers1")
    public void consumeMessage6(String message) {
        logger.info("**** -> Consumed message -> {}", message);
        productEService.updateIsPublic(message);
    }

    @KafkaListener(topics = "deleteSizeQuantityById", containerFactory = "kafkaListenerStringFactory", groupId = "tpd-loggers1")
    public void consumeMessage7(String message) {
        logger.info("**** -> Consumed message -> {}", message);
        productEService.deleteSizeQuantityById(message);
    }


    @KafkaListener(topics = "plusStock", containerFactory = "kafkaListenerStringFactory", groupId = "tpd-loggers1")
    public void consumeMessage9(String message) {
        logger.info("**** -> Consumed message -> {}", message);
        productEService.plusStockNotSize(message);
    }

    @KafkaListener(topics = "minusStock", containerFactory = "kafkaListenerStringFactory", groupId = "tpd-loggers1")
    public void consumeMessage10(String message) {
        logger.info("**** -> Consumed message -> {}", message);
        productEService.minusStockNotSize(message);
    }

}

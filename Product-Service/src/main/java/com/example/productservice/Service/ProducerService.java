package com.example.productservice.Service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

@Service
public class ProducerService<T> {

     private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private KafkaTemplate<String, T> kafkaTemplateSuperHero;

    public void sendSuperHeroMessage(String topic, T superHero) {
        logger.info("#### -> Publishing SuperHero :: {}", superHero);
        kafkaTemplateSuperHero.send(topic, superHero);
    }
    
}

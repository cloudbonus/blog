package com.github.blog.config.kafka;

import com.github.blog.controller.dto.request.PaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

/**
 * @author Raman Haurylau
 */
@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory<String, PaymentRequest> producerFactory() {
        Map<String, Object> properties = kafkaProperties.buildProducerProperties(null);
        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    public KafkaTemplate<String, PaymentRequest> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}

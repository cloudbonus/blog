package com.github.blog.service.util;

import com.github.blog.config.kafka.KafkaTopicProperties;
import com.github.blog.controller.dto.request.PaymentRequest;
import com.github.blog.repository.entity.util.KafkaTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Raman Haurylau
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class PaymentRequestProducer {

    private final KafkaTemplate<String, PaymentRequest> kafkaTemplate;
    private final KafkaTopicProperties kafkaTopicProperties;

    public PaymentRequest sendCancelPaymentRequest(PaymentRequest paymentRequest) {
        kafkaTemplate.send(kafkaTopicProperties.getTopic(KafkaTopic.CANCEL), paymentRequest);
        log.info("Payment request produced {}", paymentRequest);
        return paymentRequest;
    }

    public PaymentRequest sendProcessPaymentRequest(PaymentRequest paymentRequest) {
        kafkaTemplate.send(kafkaTopicProperties.getTopic(KafkaTopic.PROCESS), paymentRequest);
        log.info("Payment request produced {}", paymentRequest);
        return paymentRequest;
    }
}

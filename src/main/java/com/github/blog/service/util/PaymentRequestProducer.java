package com.github.blog.service.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.config.kafka.KafkaTopicProperties;
import com.github.blog.controller.dto.request.PaymentCancelRequest;
import com.github.blog.controller.dto.request.PaymentProcessRequest;
import com.github.blog.repository.entity.util.EpayKafkaTopic;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTopicProperties kafkaTopicProperties;

    @SneakyThrows
    public PaymentProcessRequest sendMessage(PaymentProcessRequest paymentProcessRequest) {
        String requestAsMessage = objectMapper.writeValueAsString(paymentProcessRequest);
        kafkaTemplate.send(kafkaTopicProperties.getTopic(EpayKafkaTopic.PROCESS), requestAsMessage);

        log.info("Payment request produced {}", requestAsMessage);

        return paymentProcessRequest;
    }

    @SneakyThrows
    public PaymentCancelRequest sendMessage(PaymentCancelRequest paymentCancelRequest) {
        String requestAsMessage = objectMapper.writeValueAsString(paymentCancelRequest);
        kafkaTemplate.send(kafkaTopicProperties.getTopic(EpayKafkaTopic.CANCEL), requestAsMessage);

        log.info("Payment request canceled {}", requestAsMessage);

        return paymentCancelRequest;
    }
}

package com.github.blog.controller.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.blog.controller.dto.request.PaymentDto;
import com.github.blog.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentConsumer {

    private static final String UPDATE_TOPIC = "${topic.names.update}";

    private final ObjectMapper objectMapper;
    private final OrderService orderService;

    @SneakyThrows
    @KafkaListener(topics = UPDATE_TOPIC)
    public void consumePaymentOnUpdate(String message) {
        log.info("Payment consumed {}", message);

        PaymentDto paymentDto = objectMapper.readValue(message, PaymentDto.class);
        orderService.updateState(paymentDto);
    }
}
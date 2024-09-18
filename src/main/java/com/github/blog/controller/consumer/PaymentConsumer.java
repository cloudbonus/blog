package com.github.blog.controller.consumer;

import com.github.blog.controller.dto.request.PaymentDto;
import com.github.blog.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentConsumer {

    private static final String UPDATE_TOPIC = "${topic.names.update}";

    private final OrderService orderService;

    @KafkaListener(topics = UPDATE_TOPIC)
    public void consumePaymentOnUpdate(PaymentDto paymentDto) {
        log.info("Payment consumed {}", paymentDto);
        orderService.updateState(paymentDto);
    }
}
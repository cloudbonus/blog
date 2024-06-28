package com.github.blog.config.kafka;

import com.github.blog.repository.entity.util.EpayKafkaTopic;
import com.github.blog.service.exception.ExceptionEnum;
import com.github.blog.service.exception.impl.CustomException;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.Optional;

/**
 * @author Raman Haurylau
 */
@Setter
@ConfigurationProperties("topic")
public class KafkaTopicProperties {
    private Map<EpayKafkaTopic, String> names;

    public String getTopic(EpayKafkaTopic epayKafkaTopic) {
        return Optional.ofNullable(names.get(epayKafkaTopic))
                .orElseThrow(() -> new CustomException(ExceptionEnum.ENDPOINT_EXCEPTION));
    }
}

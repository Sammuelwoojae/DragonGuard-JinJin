package com.dragonguard.backend.support.kafka.consumer;

import com.dragonguard.backend.domain.result.dto.kafka.ResultKafkaResponse;
import com.dragonguard.backend.global.kafka.KafkaConsumer;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class KafkaResultConsumerImpl implements KafkaConsumer<ResultKafkaResponse> {
    @Override
    public void consume(String message) {}

    @Override
    public ResultKafkaResponse readValue(String message) {
        return null;
    }
}

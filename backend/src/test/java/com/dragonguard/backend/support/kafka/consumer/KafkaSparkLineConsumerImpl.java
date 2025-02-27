package com.dragonguard.backend.support.kafka.consumer;

import com.dragonguard.backend.domain.gitrepo.dto.kafka.SparkLineKafka;
import com.dragonguard.backend.global.kafka.KafkaConsumer;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class KafkaSparkLineConsumerImpl implements KafkaConsumer<SparkLineKafka> {
    @Override
    public void consume(String message) {}

    @Override
    public SparkLineKafka readValue(String message) {
        return null;
    }
}

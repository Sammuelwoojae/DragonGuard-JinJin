package com.dragonguard.backend.support.kafka.consumer;

import com.dragonguard.backend.domain.member.dto.kafka.ContributionClientResponse;
import com.dragonguard.backend.global.kafka.KafkaConsumer;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class KafkaContributionClientConsumerImpl implements KafkaConsumer<ContributionClientResponse> {
    @Override
    public void consume(String message) {}

    @Override
    public ContributionClientResponse readValue(String message) {
        return null;
    }
}

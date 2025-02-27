package com.dragonguard.backend.domain.search.messagequeue;

import com.dragonguard.backend.domain.search.dto.kafka.KafkaSearchRequest;
import com.dragonguard.backend.global.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description 검색 결과를 요청하는 kafka producer 클래스
 */

@Component
@RequiredArgsConstructor
public class KafkaSearchScrapeProducer implements KafkaProducer<KafkaSearchRequest> {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(KafkaSearchRequest request) {
        kafkaTemplate.send("gitrank.to.scrape.result", "result", request);
    }
}

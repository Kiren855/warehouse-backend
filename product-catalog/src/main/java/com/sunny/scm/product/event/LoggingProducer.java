package com.sunny.scm.product.event;

import com.sunny.scm.common.event.LoggingEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoggingProducer {
    private final KafkaTemplate<String, LoggingEvent> template;

    @Value("${spring.kafka.topic.user-activity}")
    private String topicName;

    public void sendMessage(String userId, String username, Long companyId, String activity) {
        LoggingEvent event = LoggingEvent.builder()
                .userId(userId)
                .username(username)
                .companyId(companyId)
                .activity(activity)
                .timestamp(LocalDateTime.now())
                .build();

        template.send(topicName, userId, event);
    }
}

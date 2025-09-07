package com.sunny.scm.logging.kafka;

import com.sunny.scm.common.event.LoggingEvent;
import com.sunny.scm.logging.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoggingConsumer {

    private final LogRepository logRepository;
    private final SimpMessagingTemplate template;

    @KafkaListener(
            topics = "${spring.kafka.topic.user-activity}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void listen(LoggingEvent event) {
        log.info("Received Logging Event: {}", event);
        logRepository.save(
                com.sunny.scm.logging.entity.UserActivityLog.builder()
                        .userId(event.getUserId())
                        .companyId(event.getCompanyId())
                        .activity(event.getActivity())
                        .creationTimestamp(event.getTimestamp())
                        .build()
        );

        template.convertAndSend("/topic/logs", event);
    }

}

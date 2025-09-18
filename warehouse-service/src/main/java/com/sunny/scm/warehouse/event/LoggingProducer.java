package com.sunny.scm.warehouse.event;

import com.sunny.scm.common.event.LoggingEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoggingProducer {
    private final KafkaTemplate<String, LoggingEvent> template;

    @Value("${spring.kafka.topic.user-activity}")
    private String topicName;

    public void sendMessage(String activity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = authentication.getName();
        String username = jwt.getClaimAsString("preferred_username");
        String StringCompanyId = jwt.getClaimAsString("company_id");
        long companyId = Long.parseLong(StringCompanyId);

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

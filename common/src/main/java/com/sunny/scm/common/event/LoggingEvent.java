package com.sunny.scm.common.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoggingEvent {
    @JsonProperty("user_id")
    String userId;

    @JsonProperty("username")
    String username;

    @JsonProperty("company_id")
    Long companyId;

    @JsonProperty("activity")
    String activity;

    @JsonProperty("timestamp")
    LocalDateTime timestamp;
}

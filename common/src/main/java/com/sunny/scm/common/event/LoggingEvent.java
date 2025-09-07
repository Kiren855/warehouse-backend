package com.sunny.scm.common.event;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoggingEvent {
    String userId;
    String username;
    Long companyId;
    String activity;
    LocalDateTime timestamp;
}

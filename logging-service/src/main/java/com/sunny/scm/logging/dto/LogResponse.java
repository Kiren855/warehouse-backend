package com.sunny.scm.logging.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LogResponse {
    String username;
    String activity;
    @JsonProperty("creation_timestamp")
    LocalDateTime creationTimestamp;
}

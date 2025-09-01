package com.sunny.scm.common.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ApiResponse<T> {
    @Builder.Default
    String code = "10000";
    String message;
    T result;
}

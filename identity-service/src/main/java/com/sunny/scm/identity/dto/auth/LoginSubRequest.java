package com.sunny.scm.identity.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginSubRequest {
    @Size(min = 6, max = 20, message = "USER_INVALID")
    @NotBlank(message = "USER_REQUIRED")
    String username;

    @Size(min = 8, max = 50, message = "PASSWORD_INVALID")
    @NotBlank(message = "PASSWORD_REQUIRED")
    String password;
}

package com.sunny.scm.identity.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class RegisterRootRequest {
    // identity root info
    @JsonProperty("email")
    @Email(message = "EMAIL_INVALID")
    @NotBlank(message = "EMAIL_REQUIRED")
    String email;

    @JsonProperty("password")
    @NotBlank(message = "PASSWORD_REQUIRED")
    @Size(min = 8, max = 50, message = "PASSWORD_INVALID")
    String password;
}

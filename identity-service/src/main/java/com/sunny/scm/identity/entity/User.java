package com.sunny.scm.identity.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity(name = "users")
@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    Long id;
    UUID userId;
    String email;
    String username;
    String phone;


}

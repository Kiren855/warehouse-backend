package com.sunny.scm.identity.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum LogAction {
    CREATE_GROUP("Create group with name %s"),
    UPDATE_GROUP("Update group with name %s"),
    DELETE_GROUP("Delete group with name %s"),
    CREATE_USER("Create user with username %s"),

;
    String value;
    LogAction(String value) {
        this.value = value;
    }

    public String format(Object... args) {
        return String.format(this.value, args);
    }
}

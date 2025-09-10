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
    REMOVE_USER_FROM_GROUP("Remove %s users from group %s"),
    ADD_USER_IN_GROUP("Add %s users in group %s"),
    REMOVE_ROLE_FROM_GROUP("Remove %s roles from group %s"),
    ADD_ROLE_IN_GROUP("Add %s roles in group %s"),


;
    String value;
    LogAction(String value) {
        this.value = value;
    }

    public String format(Object... args) {
        return String.format(this.value, args);
    }
}

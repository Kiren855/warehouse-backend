package com.sunny.scm.product.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum LogAction {
    CREATE_GROUP("Create group with name %s"),
    CREATE_CATEGORY("Create category with name %s"),
    UPDATE_CATEGORY("Update category with name %s to %s"),
    DELETE_CATEGORY("Delete category with name %s"),
;
    String value;
    LogAction(String value) {
        this.value = value;
    }

    public String format(Object... args) {
        return String.format(this.value, args);
    }
}

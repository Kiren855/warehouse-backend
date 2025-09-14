package com.sunny.scm.warehouse.constant;

public enum LogAction {
    CREATE_WAREHOUSE("Create warehouse with code %s"),
    UPDATE_WAREHOUSE("Update warehouse with code %s"),
    DELETE_WAREHOUSE("Delete warehouse with code %s"),
    ;
    String value;
    LogAction(String value) {
        this.value = value;
    }

    public String format(Object... args) {
        return String.format(this.value, args);
    }
}

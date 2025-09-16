package com.sunny.scm.warehouse.constant;

public enum LogAction {
    CREATE_WAREHOUSE("Create warehouse with code %s"),
    UPDATE_WAREHOUSE("Update warehouse with code %s"),
    DELETE_WAREHOUSE("Delete warehouse with code %s"),
    CREATE_ZONE("Create zone with code %s"),
    UPDATE_ZONE("Update zone with code %s"),
    DELETE_ZONE("Delete zone with code %s"),
    CREATE_BIN("Create bin with code %s"),
    UPDATE_BIN("Update bin with code %s"),
    DELETE_BIN("Delete bin with code %s"),
    CREATE_RECEIPT("Create receipt with number %s"),

    ;
    String value;
    LogAction(String value) {
        this.value = value;
    }

    public String format(Object... args) {
        return String.format(this.value, args);
    }
}

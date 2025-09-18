package com.sunny.scm.warehouse.constant;

import jakarta.annotation.Resource;

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

    CHANGE_RECEIPT_STATUS("Change receipt %s status to %s"),
    CREATE_GROUP_RECEIPT("Create group receipt with code %s"),
    CANCEL_RECEIPT("Cancel receipt with number %s"),
    CANCEL_GROUP_RECEIPT("Cancel group receipt with code %s"),
    GENERATE_PUTAWAY_PDF("Generate putaway PDF for group receipt with code %s"),;
    String value;
    LogAction(String value) {
        this.value = value;
    }

    public String format(Object... args) {
        return String.format(this.value, args);
    }
}

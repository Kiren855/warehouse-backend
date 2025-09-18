package com.sunny.scm.product.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum LogAction {
    CREATE_CATEGORY("Create category with name %s"),
    UPDATE_CATEGORY("Update category with name %s to %s"),
    DELETE_CATEGORY("Delete category with name %s"),
    CREATE_PRODUCT("Create product with SKU: %s"),
    UPDATE_PRODUCT("Update product with SKU: %s"),
    DELETE_PRODUCT("Delete product with SKU: %s"),
    ACTIVATE_PRODUCT("Activate product with SKU: %s"),
    DEACTIVATE_PRODUCT("Deactivate product with SKU: %s"),
    ADD_PRODUCT_PACKAGE("Add package %s to product with SKU: %s"),
    REMOVE_PRODUCT_PACKAGE("Remove %s packages from product with SKU: %s"),
    UPDATE_PRODUCT_PACKAGE("Update package %s of product with SKU: %s"),;
    String value;
    LogAction(String value) {
        this.value = value;
    }

    public String format(Object... args) {
        return String.format(this.value, args);
    }
}

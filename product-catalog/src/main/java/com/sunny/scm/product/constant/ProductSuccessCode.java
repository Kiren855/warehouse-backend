package com.sunny.scm.product.constant;

import com.sunny.scm.common.base.BaseCodeError;
import org.springframework.http.HttpStatus;

public enum ProductSuccessCode implements BaseCodeError {
    CREATE_PRODUCT_SUCCESS("S20001", "Create product success", HttpStatus.OK),
    UPDATE_PRODUCT_SUCCESS("S20005", "Update product success", HttpStatus.OK),
    DELETE_PRODUCT_SUCCESS("S20006", "Delete product success", HttpStatus.OK),
    CREATE_CATEGORY_SUCCESS("S20002", "Create category success", HttpStatus.OK),
    UPDATE_CATEGORY_SUCCESS("S20003", "Update category success", HttpStatus.OK),
    DELETE_CATEGORY_SUCCESS("S20004", "Delete category success", HttpStatus.OK),

    GET_PRODUCT_DETAIL_SUCCESS("S20007", "Get product detail success", HttpStatus.OK),
    CREATE_PACKAGE_SUCCESS("S20008", "Create package success", HttpStatus.OK),
    DELETE_PACKAGE_SUCCESS("S20009", "Delete package success", HttpStatus.OK),
    UPDATE_PACKAGE_SUCCESS("S20010", "Update package success", HttpStatus.OK),
    GET_PRODUCTS_SUCCESS("S20011", "Get products success", HttpStatus.OK),
    GET_CATEGORIES_SUCCESS("S20012", "Get categories success", HttpStatus.OK),
    GET_ROOT_CATEGORIES_SUCCESS("S20013", "Get root categories success", HttpStatus.OK),
    GET_CATEGORY_TREE_SUCCESS("S20014", "Get category tree success", HttpStatus.OK),
    GET_PACKAGES_SUCCESS("S20015", "Get packages success", HttpStatus.OK),
    GET_ALL_PACKAGES_SUCCESS("S20016", "Get all packages success", HttpStatus.OK);
    String code;
    String message;
    HttpStatus httpStatus;
    ProductSuccessCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

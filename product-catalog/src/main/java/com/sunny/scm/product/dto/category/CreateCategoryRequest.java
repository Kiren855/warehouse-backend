package com.sunny.scm.product.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCategoryRequest {
    @JsonProperty("category_name")
    String categoryName;

    String description;
    @JsonProperty("parent_category_id")
    Long parentId;
}

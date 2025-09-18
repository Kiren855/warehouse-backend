package com.sunny.scm.product.helper;

import com.sunny.scm.product.constant.ProductStatus;
import com.sunny.scm.product.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class ProductSpecifications {
    public static Specification<Product> belongsToCompany(Long companyId) {
        return SpecificationUtils.equal("companyId", companyId);
    }
    public static Specification<Product> likeSkuOrName(String keyword) {
        return SpecificationUtils.orLikeIgnoreCase(List.of("productSku", "productName"), keyword);
    }

    public static Specification<Product> hasStatus(ProductStatus status) {
        return (root, query, cb) ->
                status == null ? cb.conjunction() : cb.equal(root.get("status"), status);
    }

}

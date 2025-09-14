package com.sunny.scm.product.fakedata;

import com.github.javafaker.Faker;
import com.sunny.scm.product.constant.ProductStatus;
import com.sunny.scm.product.entity.Category;
import com.sunny.scm.product.entity.Product;
import com.sunny.scm.product.repository.CategoryRepository;
import com.sunny.scm.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@RequiredArgsConstructor
public class ProductDataSeeder implements CommandLineRunner {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        List<Product> products = new ArrayList<>();

        Map<Long, Category> categories = categoryRepository.findAllById(
                LongStream.rangeClosed(18, 56).boxed().collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(Category::getId, c -> c));

        for (int i = 0; i < 6000; i++) {
            int categoryOffset = i % 39;
            Category category = categories.get(categoryOffset + 18L);

            Product p = Product.builder()
                    .productName(faker.commerce().productName())
                    .companyId(1L)
                    .category(category)
                    .unit("pcs")
                    .description(faker.lorem().sentence())
                    .productSku("SKU-C1-" + i + "-" + faker.number().digits(5))
                    .status(ProductStatus.INACTIVE)
                    .packages(new HashSet<>())
                    .build();
            products.add(p);
        }

        // Save in batch
        for (int i = 0; i < products.size(); i += 1000) {
            int end = Math.min(i + 1000, products.size());
            productRepository.saveAll(products.subList(i, end));
            productRepository.flush();
        }
    }
}

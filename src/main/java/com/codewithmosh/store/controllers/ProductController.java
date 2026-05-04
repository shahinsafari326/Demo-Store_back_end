package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
public class ProductController {
    private ProductRepository productRepository;
    private ProductMapper productMapper;

    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getAllProducts(
            @RequestParam(required = false, defaultValue = "", name = "categoryId") String findBy) {

        if (!findBy.isEmpty()) {
            if (isByte(findBy)) {
                List<ProductDto> products = productRepository
                        .findByCategory_Id(Byte.parseByte(findBy))
                        .stream()
                        .map(product -> productMapper.toDto(product))
                        .toList();

                return ResponseEntity.ok(products);
            } else {
                return ResponseEntity.badRequest().build(); // better than 404
            }
        }

        List<ProductDto> products = productRepository.findAllWithCategory()
                .stream()
                .map(product -> productMapper.toDto(product))
                .toList();

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(productMapper.toDto(product));
        }
    }

    private boolean isByte(String str) {
        try {
            Byte.parseByte(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

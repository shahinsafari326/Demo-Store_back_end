package com.demo.store.mappers;

import com.demo.store.dtos.ProductDto;
import com.demo.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

// used as interface as map strut automatically implements this class at run time!
@Mapper (componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toDto (Product product);
    Product toEntity (ProductDto productDto);

    @Mapping(target = "id", ignore = true)
    void updateProduct(ProductDto productDto, @MappingTarget Product product);

}

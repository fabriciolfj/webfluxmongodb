package com.github.fabriciolfj.demowebfluxmongo.api.mapper;

import com.github.fabriciolfj.demowebfluxmongo.api.dto.request.ProductRequestDTO;
import com.github.fabriciolfj.demowebfluxmongo.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapperRequest {

    @Mapping(target = "id", ignore = true)
    Product toDomain(final ProductRequestDTO dto);
}

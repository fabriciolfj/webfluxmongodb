package com.github.fabriciolfj.demowebfluxmongo.api.mapper;

import com.github.fabriciolfj.demowebfluxmongo.api.dto.response.ProductResponseDTO;
import com.github.fabriciolfj.demowebfluxmongo.domain.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapperResponse {

    ProductResponseDTO toDTO(final Product product);
}

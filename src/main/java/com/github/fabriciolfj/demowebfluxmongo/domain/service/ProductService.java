package com.github.fabriciolfj.demowebfluxmongo.domain.service;

import com.github.fabriciolfj.demowebfluxmongo.api.dto.request.ProductRequestDTO;
import com.github.fabriciolfj.demowebfluxmongo.api.dto.response.ProductResponseDTO;
import com.github.fabriciolfj.demowebfluxmongo.api.mapper.ProductMapperRequest;
import com.github.fabriciolfj.demowebfluxmongo.api.mapper.ProductMapperResponse;
import com.github.fabriciolfj.demowebfluxmongo.domain.model.Product;
import com.github.fabriciolfj.demowebfluxmongo.domain.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapperRequest mapperRequest;
    private final ProductMapperResponse mapperResponse;
    private final ProductRepository repository;

    public Mono<ProductResponseDTO> save(final ProductRequestDTO dto) {
        return Mono.just(mapperRequest.toDomain(dto))
                .flatMap(p ->
                    repository.save(p)
                    .flatMap(prd -> Mono.just(mapperResponse.toDTO(prd)))
                );
    }

    public Mono<ProductResponseDTO> update(final ProductRequestDTO dto, final String id) {
        return repository.findById(id)
                .flatMap(p -> {

                    if(p == null) {
                        return Mono.error(new RuntimeException());
                    }

                    var proudct = mapperRequest.toDomain(dto);
                    proudct.setId(p.getId());
                    return repository.save(proudct).flatMap(r -> Mono.just(mapperResponse.toDTO(r)));
                });
    }

    public Flux<ProductResponseDTO> findAll() {
        return repository.findAll().flatMap(p -> Flux.just(mapperResponse.toDTO(p)));
    }

    public Mono<ProductResponseDTO> findById(final String id) {
        return repository.findById(id).flatMap(p -> Mono.just(mapperResponse.toDTO(p)))
                .onErrorResume(e -> Mono.error(new RuntimeException("Product not found")));
    }

    public Mono<Void> delete(final String id) {
        return repository.deleteById(id);
    }
}

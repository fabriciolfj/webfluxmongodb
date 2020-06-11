package com.github.fabriciolfj.demowebfluxmongo.api.handler;

import com.github.fabriciolfj.demowebfluxmongo.api.dto.request.ProductRequestDTO;
import com.github.fabriciolfj.demowebfluxmongo.api.dto.response.ProductResponseDTO;
import com.github.fabriciolfj.demowebfluxmongo.domain.service.ProductService;
import com.mongodb.internal.connection.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final ProductService productService;
    private final Validator validator;

    public Mono<ServerResponse> updateProduct(final ServerRequest request) {
        return request.bodyToMono(ProductRequestDTO.class)
                .flatMap(p -> {
                    Mono<? extends ServerResponse> errors1 = validate(p);
                    if (errors1 != null) return errors1;
                    Mono<ProductResponseDTO> result = productService.update(p, request.pathVariable("id"));
                    return result.flatMap(prod -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(prod)))
                            .switchIfEmpty(ServerResponse.notFound().build())
                            .onErrorResume(ex -> ServerResponse.badRequest().build());
                });
    }

    public Mono<ServerResponse> createProduct(final ServerRequest request) {
        return request.bodyToMono(ProductRequestDTO.class)
                .flatMap(p -> {
                    Mono<? extends ServerResponse> errors1 = validate(p);
                    if (errors1 != null) return errors1;

                    return productService.save(p)
                            .flatMap(prod -> ServerResponse.created(URI.create("/products/".concat(prod.getId())))
                            .contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(prod)));
                });
    }

    private Mono<? extends ServerResponse> validate(ProductRequestDTO p) {
        var errors = new BeanPropertyBindingResult(p, ProductRequestDTO.class.getName());
        validator.validate(p, errors);

        if(errors.hasErrors()) {
            return Flux.fromIterable(errors.getFieldErrors())
                    .map(fieldError -> "Field " + fieldError.getField() + " - " + fieldError.getDefaultMessage())
                    .collectList()
                    .flatMap(list -> ServerResponse.badRequest().body(BodyInserters.fromValue(list)));
        }
        return null;
    }

    public Mono<ServerResponse> findAll(final ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.findAll(), ProductResponseDTO.class);
    }

    public Mono<ServerResponse> findById(final ServerRequest request) {
        return productService.findById(request.pathVariable("id"))
                .flatMap(p -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(p)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteById(final ServerRequest request) {
        return productService.delete(request.pathVariable("id"))
                .flatMap(p -> ServerResponse.ok().body(BodyInserters.empty()));
    }
}

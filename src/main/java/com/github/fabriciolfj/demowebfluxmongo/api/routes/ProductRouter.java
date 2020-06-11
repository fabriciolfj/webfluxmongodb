package com.github.fabriciolfj.demowebfluxmongo.api.routes;

import com.github.fabriciolfj.demowebfluxmongo.api.handler.ProductHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
@RequiredArgsConstructor
public class ProductRouter {

    @Bean
    public RouterFunction<ServerResponse> routes(final ProductHandler handler) {
        return route(GET("/products"), handler::findAll)
                .andRoute(GET("/products/{id}"), handler::findById)
                .andRoute(POST("/products"), handler::createProduct)
                .andRoute(DELETE("/products/{id}"), handler::deleteById)
                .andRoute(PUT("/products/{id}"), handler::updateProduct);
    }
}

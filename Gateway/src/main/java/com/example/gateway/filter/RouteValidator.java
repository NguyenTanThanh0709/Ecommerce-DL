package com.example.gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/api/v1/users/register",
            "/api/v1/users/authenticate",
            "/api/v1/users/refresh-token",
            "/api/v1/products/category",
            "/api/v1/products/search",
            "/api/v1/products/search/**",
            "/api/v1/notifications/callback/"
            ,"/api/v1/products/test"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}
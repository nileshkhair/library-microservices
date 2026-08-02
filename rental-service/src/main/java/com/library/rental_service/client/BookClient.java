package com.library.rental_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "book-service", url = "http://localhost:8082")
public interface BookClient {

    @GetMapping("/api/books/{id}")
    Object getBookById(@PathVariable Long id);
}

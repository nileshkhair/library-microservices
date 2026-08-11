package com.library.rental_service.client;

import com.library.rental_service.config.FeignClientConfig;
import com.library.rental_service.dto.BookResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "book-service", configuration = FeignClientConfig.class)
public interface BookClient {
    @GetMapping("/api/books/{id}")
    BookResponse getBookById(@PathVariable Long id);
}

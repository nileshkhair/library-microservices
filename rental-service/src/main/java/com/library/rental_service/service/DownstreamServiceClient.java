package com.library.rental_service.service;

import com.library.rental_service.client.BookClient;
import com.library.rental_service.client.UserClient;
import com.library.rental_service.dto.BookResponse;
import com.library.rental_service.dto.UserResponse;
import com.library.rental_service.exception.ServiceUnavailableException;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DownstreamServiceClient {

    private final UserClient userClient;
    private final BookClient bookClient;

    public DownstreamServiceClient(
            UserClient userClient,
            BookClient bookClient) {

        this.userClient = userClient;
        this.bookClient = bookClient;
    }

    @CircuitBreaker(
            name = "userServiceCB",
            fallbackMethod = "userServiceFallback"
    )
    public UserResponse callUserService(Long userId) {

        log.info("Calling User Service for userId: {}", userId);

        return userClient.getUserById(userId);
    }

    public UserResponse userServiceFallback(
            Long userId,
            Throwable throwable) {

        log.error(
                "USER-SERVICE FALLBACK. userId={}, cause={}",
                userId,
                throwable.toString()
        );

        // 404 must NOT become 503
        if (throwable instanceof FeignException.NotFound) {
            throw (FeignException.NotFound) throwable;
        }

        throw new ServiceUnavailableException(
                "User Service is temporarily unavailable."
        );
    }


    @CircuitBreaker(
            name = "bookServiceCB",
            fallbackMethod = "bookServiceFallback"
    )
    public BookResponse callBookService(Long bookId) {

        log.info("Calling Book Service for bookId: {}", bookId);

        return bookClient.getBookById(bookId);
    }

    public BookResponse bookServiceFallback(
            Long bookId,
            Throwable throwable) {

        log.error(
                "BOOK-SERVICE FALLBACK. bookId={}, cause={}",
                bookId,
                throwable.toString()
        );

        if (throwable instanceof FeignException.NotFound) {
            throw (FeignException.NotFound) throwable;
        }

        throw new ServiceUnavailableException(
                "Book Service is temporarily unavailable."
        );
    }
}
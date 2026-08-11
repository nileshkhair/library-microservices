package com.library.rental_service.service;

import com.library.rental_service.exception.ResourceNotFoundException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RentalValidationService {

    private final DownstreamServiceClient downstreamServiceClient;

    public RentalValidationService(
            DownstreamServiceClient downstreamServiceClient) {

        this.downstreamServiceClient = downstreamServiceClient;
    }

    public void validateUser(Long userId) {

        log.info("userId : {}", userId);

        try {
            downstreamServiceClient.callUserService(userId);
        } catch (FeignException.NotFound e) {

            log.warn("User not found. userId={}", userId);

            throw new ResourceNotFoundException("User not found");
        }
    }

    public void validateBook(Long bookId) {

        log.info("bookId : {}", bookId);

        try {
            downstreamServiceClient.callBookService(bookId);
        } catch (FeignException.NotFound e) {

            log.warn("Book not found. bookId={}", bookId);

            throw new ResourceNotFoundException("Book not found");
        }
    }
}
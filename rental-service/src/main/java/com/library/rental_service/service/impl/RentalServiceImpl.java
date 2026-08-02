package com.library.rental_service.service.impl;

import com.library.rental_service.client.BookClient;
import com.library.rental_service.client.UserClient;
import com.library.rental_service.dto.RentalRequest;
import com.library.rental_service.dto.RentalResponse;
import com.library.rental_service.entity.Rental;
import com.library.rental_service.exception.ResourceNotFoundException;
import com.library.rental_service.repository.RentalRepository;
import com.library.rental_service.service.RentalService;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
//@RequiredArgsConstructor  -- constructor is available
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;
    private final UserClient userClient;
    private final BookClient bookClient;
    // private final RestTemplate restTemplate;


    public RentalServiceImpl(RentalRepository rentalRepository, UserClient userClient, BookClient bookClient) {
        this.rentalRepository = rentalRepository;
        this.userClient = userClient;
        this.bookClient = bookClient;
    }

    @Override
    public RentalResponse createRental(RentalRequest rentalRequest) {
        /*
        ===== using RestTemplate
        try {
            restTemplate.getForObject("http://localhost:8081/api/users/" + rentalRequest.getUserId(), Object.class);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new ResourceNotFoundException("User not found");
        }
        try {
            restTemplate.getForObject("http://localhost:8082/api/books/" + rentalRequest.getBookId(), Object.class);

        } catch (HttpClientErrorException.NotFound exception) {
            throw new ResourceNotFoundException("Book not found");
        }
        */

        /*
        NEW OpenFeign approach
        Previously this service used RestTemplate.
        Refactored to OpenFeign
        OpenFeign provides a cleaner, interface-based way to call other microservices.
        */
        try {
            userClient.getUserById(rentalRequest.getUserId());
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("User not found");
        }
        try {
            bookClient.getBookById(rentalRequest.getBookId());
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Book not found");
        }

        Rental rental = Rental.builder()
                .userId(rentalRequest.getUserId())
                .bookId(rentalRequest.getBookId())
                .rentalDate(LocalDate.now())
                .returnDate(null)
                .build();
        Rental save = rentalRepository.save(rental);
        return RentalResponse.builder()
                .id(save.getId())
                .userId(save.getUserId())
                .bookId(save.getBookId())
                .rentalDate(save.getRentalDate())
                .returnDate(save.getReturnDate())
                .build();
    }

    @Override
    public RentalResponse getRentalById(Long id) {
        Rental rental = rentalRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Rental not found"));
        return RentalResponse.builder()
                .id(rental.getId())
                .userId(rental.getUserId())
                .bookId(rental.getBookId())
                .rentalDate(rental.getRentalDate())
                .returnDate(rental.getReturnDate())
                .build();
    }

    @Override
    public List<RentalResponse> getAllRentals() {
        return rentalRepository.findAll()
                .stream()
                .map(rental -> RentalResponse.builder()
                        .id(rental.getId())
                        .userId(rental.getUserId())
                        .bookId(rental.getBookId())
                        .rentalDate(rental.getRentalDate())
                        .returnDate(rental.getReturnDate())
                        .build())
                .toList();
    }

    @Override
    public RentalResponse returnBook(Long id) {
        Rental rental = rentalRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Rental not found"));
        rental.setReturnDate(LocalDate.now());
        Rental save = rentalRepository.save(rental);
        return RentalResponse.builder()
                .id(save.getId())
                .userId(save.getUserId())
                .bookId(save.getBookId())
                .rentalDate(save.getRentalDate())
                .returnDate(save.getReturnDate())
                .build();
    }

    @Override
    public void deleteRental(Long id) {
        Rental rental = rentalRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Rental not found"));
        rentalRepository.delete(rental);
    }
}

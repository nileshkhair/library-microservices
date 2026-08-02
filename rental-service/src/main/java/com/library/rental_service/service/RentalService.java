package com.library.rental_service.service;

import com.library.rental_service.dto.RentalRequest;
import com.library.rental_service.dto.RentalResponse;

import java.util.List;

public interface RentalService {

    RentalResponse createRental(RentalRequest rentalRequest);
    RentalResponse getRentalById(Long id);
    List<RentalResponse> getAllRentals();
    RentalResponse returnBook(Long id);
    void deleteRental(Long id);
}

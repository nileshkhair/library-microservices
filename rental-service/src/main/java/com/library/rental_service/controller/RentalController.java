package com.library.rental_service.controller;

import com.library.rental_service.dto.RentalRequest;
import com.library.rental_service.dto.RentalResponse;
import com.library.rental_service.service.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RentalResponse createRental(@Valid @RequestBody RentalRequest rentalRequest) {
        return rentalService.createRental(rentalRequest);
    }

    @GetMapping("/{id}")
    public RentalResponse getRentalById(@PathVariable Long id) {
        log.info("Rental id: {}",id);
        return rentalService.getRentalById(id);
    }

    @GetMapping
    public List<RentalResponse> getAllRentals() {
        return rentalService.getAllRentals();
    }

    @PutMapping("/{id}/return")
    public RentalResponse returnBook(@PathVariable Long id) {
        return rentalService.returnBook(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRental(@PathVariable Long id) {
        rentalService.deleteRental(id);
    }
}

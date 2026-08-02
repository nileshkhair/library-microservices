package com.library.rental_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalRequest {
    @NotNull(message = "User id is required")
    private Long userId;
    @NotNull(message = "Book id is required")
    private Long bookId;
}


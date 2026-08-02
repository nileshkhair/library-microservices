package com.library.book_service.service;

import com.library.book_service.dto.BookRequest;
import com.library.book_service.dto.BookResponse;

import java.util.List;

public interface BookService {
    BookResponse createBook(BookRequest bookRequest);
    BookResponse getBookById(Long id);
    List<BookResponse> getAllBooks();
    BookResponse updateBook(Long id, BookRequest bookRequest);
    void deleteBook(Long id);
}

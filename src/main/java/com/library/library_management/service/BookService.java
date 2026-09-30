package com.library.library_management.service;

import com.library.library_management.dto.BookDTO;

import java.util.List;

public interface BookService {

    BookDTO createBook(BookDTO bookDTO);

    BookDTO getBookById(Long id);

    List<BookDTO> getAllBooks();

    BookDTO updateBook(Long id, BookDTO bookDTO);

    void deleteBook(Long id);

    List<BookDTO> getBooksByGenre(String genre);

    List<BookDTO> searchBooksByTitle(String title);
}
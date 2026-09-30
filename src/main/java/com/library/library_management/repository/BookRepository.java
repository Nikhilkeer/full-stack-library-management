package com.library.library_management.repository;

import com.library.library_management.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Custom query method - Spring khud query bana dega naam dekh kar
    Optional<Book> findByIsbn(String isbn);

    List<Book> findByGenre(String genre);

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorId(Long authorId);
}
package com.library.library_management.service.impl;

import com.library.library_management.dto.BookDTO;
import com.library.library_management.entity.Author;
import com.library.library_management.entity.Book;
import com.library.library_management.exception.ResourceNotFoundException;
import com.library.library_management.repository.AuthorRepository;
import com.library.library_management.repository.BookRepository;
import com.library.library_management.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    // Constructor Injection - dono repository inject ho rahe hain
    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    @Transactional
    public BookDTO createBook(BookDTO bookDTO) {
        // Pehle check karo ki Author exist karta hai ya nahi
        Author author = authorRepository.findById(bookDTO.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Author not found with id: " + bookDTO.getAuthorId()));

        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setIsbn(bookDTO.getIsbn());
        book.setGenre(bookDTO.getGenre());
        book.setTotalCopies(bookDTO.getTotalCopies());
        book.setAvailableCopies(bookDTO.getTotalCopies()); // initially available = total
        book.setAuthor(author);

        Book savedBook = bookRepository.save(book);
        return mapToDTO(savedBook);
    }

    @Override
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return mapToDTO(book);
    }

    @Override
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setIsbn(bookDTO.getIsbn());
        existingBook.setGenre(bookDTO.getGenre());
        existingBook.setTotalCopies(bookDTO.getTotalCopies());
        existingBook.setAvailableCopies(bookDTO.getAvailableCopies());

        // Agar author bhi change karna ho
        if (bookDTO.getAuthorId() != null) {
            Author author = authorRepository.findById(bookDTO.getAuthorId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Author not found with id: " + bookDTO.getAuthorId()));
            existingBook.setAuthor(author);
        }

        Book updatedBook = bookRepository.save(existingBook);
        return mapToDTO(updatedBook);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        bookRepository.delete(book);
    }

    @Override
    public List<BookDTO> getBooksByGenre(String genre) {
        return bookRepository.findByGenre(genre)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Entity → DTO (Author ka data bhi flatten karke daal rahe hain)
    private BookDTO mapToDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setGenre(book.getGenre());
        dto.setTotalCopies(book.getTotalCopies());
        dto.setAvailableCopies(book.getAvailableCopies());
        dto.setAuthorId(book.getAuthor().getId());
        dto.setAuthorName(book.getAuthor().getName());
        return dto;
    }
}
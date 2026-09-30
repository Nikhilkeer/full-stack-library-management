package com.library.library_management.service;

import com.library.library_management.dto.BookDTO;
import com.library.library_management.entity.Author;
import com.library.library_management.entity.Book;
import com.library.library_management.exception.ResourceNotFoundException;
import com.library.library_management.repository.AuthorRepository;
import com.library.library_management.repository.BookRepository;
import com.library.library_management.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Author testAuthor;
    private Book testBook;
    private BookDTO testBookDTO;

    @BeforeEach
    void setUp() {
        testAuthor = new Author();
        testAuthor.setId(1L);
        testAuthor.setName("J.K. Rowling");

        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Harry Potter");
        testBook.setIsbn("9780439708180");
        testBook.setGenre("Fantasy");
        testBook.setTotalCopies(5);
        testBook.setAvailableCopies(5);
        testBook.setAuthor(testAuthor);

        testBookDTO = new BookDTO();
        testBookDTO.setTitle("Harry Potter");
        testBookDTO.setIsbn("9780439708180");
        testBookDTO.setGenre("Fantasy");
        testBookDTO.setTotalCopies(5);
        testBookDTO.setAuthorId(1L);
    }

    @Test
    void testCreateBook_Success() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(testAuthor));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        BookDTO result = bookService.createBook(testBookDTO);

        assertNotNull(result);
        assertEquals("Harry Potter", result.getTitle());
        assertEquals(5, result.getAvailableCopies());
        assertEquals("J.K. Rowling", result.getAuthorName());

        verify(authorRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testCreateBook_AuthorNotFound_ThrowsException() {
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());
        testBookDTO.setAuthorId(99L);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> bookService.createBook(testBookDTO)
        );

        assertEquals("Author not found with id: 99", exception.getMessage());

        // Important: confirm karo ki Author na milne par, Book save HI NAHI hui
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void testGetBookById_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        BookDTO result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals("Harry Potter", result.getTitle());
        assertEquals(5, result.getTotalCopies());
    }

    @Test
    void testGetBookById_NotFound_ThrowsException() {
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> bookService.getBookById(999L)
        );
    }

    @Test
    void testDeleteBook_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        doNothing().when(bookRepository).delete(testBook);

        assertDoesNotThrow(() -> bookService.deleteBook(1L));

        verify(bookRepository, times(1)).delete(testBook);
    }
}
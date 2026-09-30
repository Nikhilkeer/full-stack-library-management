package com.library.library_management.service;

import com.library.library_management.dto.AuthorDTO;
import com.library.library_management.entity.Author;
import com.library.library_management.exception.ResourceNotFoundException;
import com.library.library_management.repository.AuthorRepository;
import com.library.library_management.service.impl.AuthorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private Author testAuthor;

    @BeforeEach
    void setUp() {
        testAuthor = new Author();
        testAuthor.setId(1L);
        testAuthor.setName("J.K. Rowling");
        testAuthor.setEmail("jk@example.com");
        testAuthor.setNationality("British");
    }

    @Test
    void testGetAuthorById_Success() {
        // Arrange (setup): jab repository ko id=1 se call kiya jaaye, ye author return karo
        when(authorRepository.findById(1L)).thenReturn(Optional.of(testAuthor));

        // Act (actual method call)
        AuthorDTO result = authorService.getAuthorById(1L);

        // Assert (verify karo result sahi hai)
        assertNotNull(result);
        assertEquals("J.K. Rowling", result.getName());
        assertEquals("British", result.getNationality());

        // Verify karo ki repository ka findById exactly 1 baar call hua
        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAuthorById_NotFound_ThrowsException() {
        // Arrange: jab id=99 se call ho, empty Optional return karo (author exist nahi karta)
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert: exception throw hona chahiye
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> authorService.getAuthorById(99L)
        );

        assertEquals("Author not found with id: 99", exception.getMessage());
    }

    @Test
    void testCreateAuthor_Success() {
        AuthorDTO inputDto = new AuthorDTO(null, "New Author", "new@example.com", "American");

        when(authorRepository.save(any(Author.class))).thenReturn(testAuthor);

        AuthorDTO result = authorService.createAuthor(inputDto);

        assertNotNull(result);
        assertEquals("J.K. Rowling", result.getName()); // Saved entity ka data return hua
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void testGetAllAuthors_ReturnsListOfAuthors() {
        when(authorRepository.findAll()).thenReturn(List.of(testAuthor));

        List<AuthorDTO> result = authorService.getAllAuthors();

        assertEquals(1, result.size());
        assertEquals("J.K. Rowling", result.get(0).getName());
    }

    @Test
    void testDeleteAuthor_Success() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(testAuthor));
        doNothing().when(authorRepository).delete(testAuthor);

        assertDoesNotThrow(() -> authorService.deleteAuthor(1L));

        verify(authorRepository, times(1)).delete(testAuthor);
    }
}
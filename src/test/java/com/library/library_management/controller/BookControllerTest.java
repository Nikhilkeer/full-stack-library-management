package com.library.library_management.controller;

import com.library.library_management.config.JwtAuthFilter;
import com.library.library_management.config.SecurityConfig;
import com.library.library_management.dto.BookDTO;
import com.library.library_management.exception.ResourceNotFoundException;
import com.library.library_management.service.BookService;
import com.library.library_management.service.CustomUserDetailsService;
import com.library.library_management.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import({SecurityConfig.class, JwtAuthFilter.class})
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private BookService bookService;

    // SecurityConfig / JwtAuthFilter ki dependencies (real nahi chahiye, mock kaafi hai)
    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    private BookDTO testBookDTO;

    @BeforeEach
    void setUp() {
        testBookDTO = new BookDTO();
        testBookDTO.setId(1L);
        testBookDTO.setTitle("Harry Potter");
        testBookDTO.setIsbn("9780439708180");
        testBookDTO.setGenre("Fantasy");
        testBookDTO.setTotalCopies(5);
        testBookDTO.setAvailableCopies(5);
        testBookDTO.setAuthorId(1L);
        testBookDTO.setAuthorName("J.K. Rowling");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateBook_Success() throws Exception {
        when(bookService.createBook(any(BookDTO.class))).thenReturn(testBookDTO);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBookDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Harry Potter"))
                .andExpect(jsonPath("$.availableCopies").value(5));
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    void testCreateBook_AsMember_Forbidden() throws Exception {
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBookDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void testGetBookById_Success() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(testBookDTO);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Harry Potter"))
                .andExpect(jsonPath("$.isbn").value("9780439708180"));
    }

    @Test
    @WithMockUser
    void testGetBookById_NotFound() throws Exception {
        when(bookService.getBookById(999L))
                .thenThrow(new ResourceNotFoundException("Book not found with id: 999"));

        mockMvc.perform(get("/api/books/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found with id: 999"));
    }

    @Test
    @WithMockUser
    void testGetAllBooks_Success() throws Exception {
        when(bookService.getAllBooks()).thenReturn(List.of(testBookDTO));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Harry Potter"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateBook_InvalidData_ReturnsBadRequest() throws Exception {
        BookDTO invalidBook = new BookDTO();
        invalidBook.setTitle("");
        invalidBook.setIsbn("123");
        invalidBook.setTotalCopies(-5);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidBook)))
                .andExpect(status().isBadRequest());
    }
}
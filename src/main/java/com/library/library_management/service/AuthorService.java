package com.library.library_management.service;

import com.library.library_management.dto.AuthorDTO;

import java.util.List;

public interface AuthorService {

    AuthorDTO createAuthor(AuthorDTO authorDTO);

    AuthorDTO getAuthorById(Long id);

    List<AuthorDTO> getAllAuthors();

    AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO);

    void deleteAuthor(Long id);
}
package com.library.library_management.service.impl;

import com.library.library_management.dto.AuthorDTO;
import com.library.library_management.entity.Author;
import com.library.library_management.exception.ResourceNotFoundException;
import com.library.library_management.repository.AuthorRepository;
import com.library.library_management.service.AuthorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    // Constructor Injection
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    @Transactional
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        Author author = mapToEntity(authorDTO);
        Author savedAuthor = authorRepository.save(author);
        return mapToDTO(savedAuthor);
    }

    @Override
    public AuthorDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        return mapToDTO(author);
    }

    @Override
    public List<AuthorDTO> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));

        existingAuthor.setName(authorDTO.getName());
        existingAuthor.setEmail(authorDTO.getEmail());
        existingAuthor.setNationality(authorDTO.getNationality());

        Author updatedAuthor = authorRepository.save(existingAuthor);
        return mapToDTO(updatedAuthor);
    }

    @Override
    @Transactional
    public void deleteAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        authorRepository.delete(author);
    }

    // Helper method: Entity → DTO
    private AuthorDTO mapToDTO(Author author) {
        return new AuthorDTO(
                author.getId(),
                author.getName(),
                author.getEmail(),
                author.getNationality()
        );
    }

    // Helper method: DTO → Entity
    private Author mapToEntity(AuthorDTO authorDTO) {
        Author author = new Author();
        author.setName(authorDTO.getName());
        author.setEmail(authorDTO.getEmail());
        author.setNationality(authorDTO.getNationality());
        return author;
    }
}
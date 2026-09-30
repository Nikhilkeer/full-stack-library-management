package com.library.library_management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDTO {

    private Long id;

    @NotBlank(message = "Name is requried")
    private String name;

    @Email(message = "Please provide a valid email addredd")
    private String email;


    private String nationality;
}
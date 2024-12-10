package com.example.library.management.dto.book;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRequestDto {

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 100, message = "Author name must not exceed 100 characters")
    private String author;

    @NotNull(message = "Publication year is required")
    @Min(value = 1000, message = "Publication year must be a valid year (e.g., 1000 or later)")
    @Max(value = 2100, message = "Publication year cannot be in the future")
    private Integer publicationYear;

    @NotBlank(message = "ISBN is required")
    @Size(min = 10, max = 13, message = "ISBN must be between 10 and 13 characters")
    @Pattern(regexp = "^[0-9Xx-]+$", message = "ISBN must contain only digits, hyphens, or 'X'")
    private String isbn;

    @Size(max = 100, message = "Publisher name must not exceed 100 characters")
    private String publisher;

    @Size(max = 50, message = "Language must not exceed 50 characters")
    private String language;

    @Min(value = 1, message = "Total pages must be at least 1")
    @Max(value = 10000, message = "Total pages must not exceed 10,000")
    private Integer totalPages;

    @Size(max = 50, message = "Genre must not exceed 50 characters")
    private String genre;

    @PastOrPresent(message = "Added date cannot be in the future")
    private LocalDate addedDate;

    @NotNull(message = "Availability status is required")
    private Boolean isAvailable;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
}

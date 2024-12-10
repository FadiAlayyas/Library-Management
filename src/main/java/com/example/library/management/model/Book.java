package com.example.library.management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    @Column(nullable = false, length = 255)
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 100, message = "Author name must not exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String author;

    @NotNull(message = "Publication year is required")
    @Column(name = "publication_year", nullable = false)
    private Integer publicationYear;

    @NotBlank(message = "ISBN is required")
    @Size(max = 13, message = "ISBN must not exceed 13 characters")
    @Column(nullable = false, unique = true, length = 13)
    private String isbn;

    @Column(name = "publisher", length = 100)
    private String publisher;

    @Column(name = "language", length = 50)
    private String language;

    @Column(name = "total_pages")
    private Integer totalPages;

    @Column(name = "genre", length = 50)
    private String genre;

    @Column(name = "added_date")
    private LocalDate addedDate;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    @Column(name = "description", length = 1000)
    private String description;
}

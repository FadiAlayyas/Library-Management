package com.example.library.management.dto.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDto {
    private Long id;
    public String title;
    private String author;
    private Integer publicationYear;
    private String isbn;
    private String publisher;
    private String language;
    private Integer totalPages;
    private String genre;
    private LocalDate addedDate;
    private Boolean isAvailable;
    private String description;
}

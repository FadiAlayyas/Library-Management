package com.example.library.management.dto.borrowing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowingResponseDto {

    private Long id;
    private Long bookId;
    private String bookTitle;
    private Long patronId;
    private String patronName;
    private LocalDate borrowDate;
    private LocalDate returnDate;
}

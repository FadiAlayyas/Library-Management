package com.example.library.management.controller;

import com.example.library.management.dto.borrowing.BorrowingRequestDto;
import com.example.library.management.dto.borrowing.BorrowingResponseDto;
import com.example.library.management.service.BorrowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrowings")
public class BorrowingController {

    @Autowired
    private BorrowingService borrowingService;

    @PostMapping("/borrow")
    public ResponseEntity<BorrowingResponseDto> borrowBook(@Validated @RequestBody BorrowingRequestDto requestDto) {
        BorrowingResponseDto responseDto = borrowingService.borrowBook(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowingResponseDto> returnBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        BorrowingResponseDto responseDto = borrowingService.returnBook(bookId, patronId);
        return ResponseEntity.ok(responseDto);
    }
}

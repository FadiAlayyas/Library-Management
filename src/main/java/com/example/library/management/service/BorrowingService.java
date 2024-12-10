package com.example.library.management.service;

import com.example.library.management.dto.borrowing.BorrowingRequestDto;
import com.example.library.management.dto.borrowing.BorrowingResponseDto;
import com.example.library.management.model.Book;
import com.example.library.management.model.Borrowing;
import com.example.library.management.model.Patron;
import com.example.library.management.repository.BookRepository;
import com.example.library.management.repository.BorrowingRepository;
import com.example.library.management.repository.PatronRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class BorrowingService {

    @Autowired
    private BorrowingRepository borrowingRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PatronRepository patronRepository;

    // Use @Transactional to ensure data consistency across multiple operations
    @Transactional
    public BorrowingResponseDto borrowBook(BorrowingRequestDto requestDto) {
        // Fetching the book and checking its availability
        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + requestDto.getBookId()));

        if (!book.getIsAvailable()) {
            throw new IllegalStateException("Book is not available for borrowing.");
        }

        // Fetching the patron
        Patron patron = patronRepository.findById(requestDto.getPatronId())
                .orElseThrow(() -> new EntityNotFoundException("Patron not found with ID: " + requestDto.getPatronId()));

        // Creating a new borrowing record
        Borrowing borrowing = new Borrowing();
        borrowing.setBook(book);
        borrowing.setPatron(patron);
        borrowing.setBorrowDate(LocalDate.now());

        // Marking the book as unavailable
        book.setIsAvailable(false);
        bookRepository.save(book); // Save updated book status

        // Saving the borrowing record
        Borrowing savedBorrowing = borrowingRepository.save(borrowing);

        // Returning the response DTO
        return convertToResponseDto(savedBorrowing);
    }

    // Use @Transactional to ensure both return and book availability update happen together
    @Transactional
    public BorrowingResponseDto returnBook(Long bookId, Long patronId) {
        // Fetching the borrowing record for the given book and patron
        Borrowing borrowing = borrowingRepository.findByBookIdAndPatronIdAndReturnDateIsNull(bookId, patronId)
                .orElseThrow(() -> new EntityNotFoundException("No active borrowing record found for book ID: " + bookId +
                        " and patron ID: " + patronId));

        // Updating the return date and marking the book as available
        borrowing.setReturnDate(LocalDate.now());
        borrowing.getBook().setIsAvailable(true);

        // Saving both the borrowing record and the updated book status
        borrowingRepository.save(borrowing);
        bookRepository.save(borrowing.getBook());

        // Returning the response DTO
        return convertToResponseDto(borrowing);
    }

    // Helper method to convert Borrowing entity to BorrowingResponseDto
    private BorrowingResponseDto convertToResponseDto(Borrowing borrowing) {
        BorrowingResponseDto responseDto = new BorrowingResponseDto();
        responseDto.setId(borrowing.getId());
        responseDto.setBookId(borrowing.getBook().getId());
        responseDto.setBookTitle(borrowing.getBook().getTitle());
        responseDto.setPatronId(borrowing.getPatron().getId());
        responseDto.setPatronName(borrowing.getPatron().getName());
        responseDto.setBorrowDate(borrowing.getBorrowDate());
        responseDto.setReturnDate(borrowing.getReturnDate());
        return responseDto;
    }
}

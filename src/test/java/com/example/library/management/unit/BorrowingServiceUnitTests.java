package com.example.library.management.unit;

import com.example.library.management.dto.borrowing.BorrowingRequestDto;
import com.example.library.management.dto.borrowing.BorrowingResponseDto;
import com.example.library.management.model.Book;
import com.example.library.management.model.Borrowing;
import com.example.library.management.model.Patron;
import com.example.library.management.repository.BookRepository;
import com.example.library.management.repository.BorrowingRepository;
import com.example.library.management.repository.PatronRepository;
import com.example.library.management.service.BorrowingService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BorrowingServiceUnitTests {

    @Mock
    private BorrowingRepository borrowingRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private BorrowingService borrowingService;

    private Book book;
    private Patron patron;
    private BorrowingRequestDto borrowingRequestDto;
    private Borrowing borrowing;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setIsAvailable(true);

        patron = new Patron();
        patron.setId(1L);
        patron.setName("Test Patron");

        borrowingRequestDto = new BorrowingRequestDto();
        borrowingRequestDto.setBookId(1L);
        borrowingRequestDto.setPatronId(1L);

        borrowing = new Borrowing();
        borrowing.setId(1L);
        borrowing.setBook(book);
        borrowing.setPatron(patron);
        borrowing.setBorrowDate(LocalDate.now());
    }

    @Test
    void borrowBook_shouldReturnBorrowingResponseDto() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(borrowingRepository.save(any(Borrowing.class))).thenReturn(borrowing);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BorrowingResponseDto response = borrowingService.borrowBook(borrowingRequestDto);

        assertNotNull(response);
        assertEquals(1L, response.getBookId());
        assertEquals("Test Book", response.getBookTitle());
        assertEquals(1L, response.getPatronId());
        assertEquals("Test Patron", response.getPatronName());
        assertEquals(LocalDate.now(), response.getBorrowDate());
        assertNull(response.getReturnDate());
    }

    @Test
    void borrowBook_shouldThrowEntityNotFoundExceptionWhenBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                borrowingService.borrowBook(borrowingRequestDto));
        assertEquals("Book not found with ID: 1", exception.getMessage());
    }

    @Test
    void borrowBook_shouldThrowIllegalStateExceptionWhenBookNotAvailable() {
        book.setIsAvailable(false); // Set book as unavailable
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                borrowingService.borrowBook(borrowingRequestDto));
        assertEquals("Book is not available for borrowing.", exception.getMessage());
    }

    @Test
    void borrowBook_shouldThrowEntityNotFoundExceptionWhenPatronNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                borrowingService.borrowBook(borrowingRequestDto));
        assertEquals("Patron not found with ID: 1", exception.getMessage());
    }

    @Test
    void returnBook_shouldReturnBorrowingResponseDto() {
        borrowing.setReturnDate(LocalDate.now());
        when(borrowingRepository.findByBookIdAndPatronIdAndReturnDateIsNull(1L, 1L))
                .thenReturn(Optional.of(borrowing));
        when(borrowingRepository.save(any(Borrowing.class))).thenReturn(borrowing);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BorrowingResponseDto response = borrowingService.returnBook(1L, 1L);

        assertNotNull(response);
        assertEquals(1L, response.getBookId());
        assertEquals("Test Book", response.getBookTitle());
        assertEquals(1L, response.getPatronId());
        assertEquals("Test Patron", response.getPatronName());
        assertNotNull(response.getReturnDate());
    }

    @Test
    void returnBook_shouldThrowEntityNotFoundExceptionWhenBorrowingNotFound() {
        when(borrowingRepository.findByBookIdAndPatronIdAndReturnDateIsNull(1L, 1L))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                borrowingService.returnBook(1L, 1L));
        assertEquals("No active borrowing record found for book ID: 1 and patron ID: 1", exception.getMessage());
    }

    @Test
    void returnBook_shouldThrowDataAccessExceptionWhenSavingFails() {
        borrowing.setReturnDate(LocalDate.now());
        when(borrowingRepository.findByBookIdAndPatronIdAndReturnDateIsNull(1L, 1L))
                .thenReturn(Optional.of(borrowing));
        when(borrowingRepository.save(any(Borrowing.class))).thenThrow(new DataAccessException("Database error") {});

        DataAccessException exception = assertThrows(DataAccessException.class, () ->
                borrowingService.returnBook(1L, 1L));
        assertEquals("Database error", exception.getMessage());
    }
}

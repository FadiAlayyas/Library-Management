package com.example.library.management.unit;

import com.example.library.management.dto.book.BookRequestDto;
import com.example.library.management.dto.book.BookResponseDto;
import com.example.library.management.exception.ResourceNotFoundException;
import com.example.library.management.model.Book;
import com.example.library.management.repository.BookRepository;
import com.example.library.management.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookServiceUnitTests {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private BookRequestDto bookRequestDto;
    private BookResponseDto bookResponseDto;

    @BeforeEach
    public void setup() {
        book = new Book(1L, "Book Title", "Author Name", 2022, "123456789", "Publisher", "English", 300, "Fiction", LocalDate.now(), true, "Description");
        bookRequestDto = new BookRequestDto("Book Title", "Author Name", 2022, "123456789", "Publisher", "English", 300, "Fiction", LocalDate.now(), true, "Description");
        bookResponseDto = new BookResponseDto(1L, "Book Title", "Author Name", 2022, "123456789", "Publisher", "English", 300, "Fiction", LocalDate.now(), true, "Description");
    }

    @Test
    public void getAllBooksTest() {
        // Arrange
        given(bookRepository.findAll()).willReturn(List.of(book));

        // Act
        List<BookResponseDto> bookList = bookService.getAllBooks();

        // Assert
        assertThat(bookList).isNotNull();
        assertThat(bookList.size()).isEqualTo(1);
        assertThat(bookList.get(0).getTitle()).isEqualTo("Book Title");
    }

    @Test
    public void getBookByIdTest() {
        // Arrange
        given(bookRepository.findById(1L)).willReturn(Optional.of(book));

        // Act
        BookResponseDto foundBook = bookService.getBookById(1L);

        // Assert
        assertThat(foundBook).isNotNull();
        assertThat(foundBook.getId()).isEqualTo(1L);
        assertThat(foundBook.getTitle()).isEqualTo("Book Title");
    }

    @Test
    public void getBookById_NotFoundTest() {
        // Arrange
        given(bookRepository.findById(1L)).willReturn(Optional.empty());

        // Act & Assert
        try {
            bookService.getBookById(1L);
        } catch (ResourceNotFoundException e) {
            assertThat(e.getMessage()).contains("Book not found");
        }
    }

    @Test
    public void addBookTest() {
        // Arrange
        Book savedBook = new Book(1L, "Book Title", "Author Name", 2022, "123456789", "Publisher", "English", 300, "Fiction", LocalDate.now(), true, "Description");
        given(bookRepository.save(any(Book.class))).willReturn(savedBook);

        // Act
        BookResponseDto createdBook = bookService.addBook(bookRequestDto);

        // Assert
        assertThat(createdBook).isNotNull();
        assertThat(createdBook.getId()).isEqualTo(1L);
        assertThat(createdBook.getTitle()).isEqualTo("Book Title");
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void updateBookTest() {
        // Arrange
        Book existingBook = new Book(1L, "Book Title", "Author Name", 2022, "123456789", "Publisher", "English", 300, "Fiction", LocalDate.now(), true, "Description");
        BookRequestDto updateRequestDto = new BookRequestDto("Updated Title", "Updated Author", 2023, "987654321", "Updated Publisher", "Spanish", 350, "Non-Fiction", LocalDate.now(), false, "Updated Description");

        given(bookRepository.findById(1L)).willReturn(Optional.of(existingBook));
        given(bookRepository.save(any(Book.class))).willReturn(existingBook);

        // Act
        BookResponseDto updatedBook = bookService.updateBook(1L, updateRequestDto);

        // Assert
        assertThat(updatedBook).isNotNull();
        assertThat(updatedBook.getTitle()).isEqualTo("Updated Title");
        assertThat(updatedBook.getAuthor()).isEqualTo("Updated Author");
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void updateBook_NotFoundTest() {
        // Arrange
        BookRequestDto updateRequestDto = new BookRequestDto("Updated Title", "Updated Author", 2023, "987654321", "Updated Publisher", "Spanish", 350, "Non-Fiction", LocalDate.now(), false, "Updated Description");

        given(bookRepository.findById(1L)).willReturn(Optional.empty());

        // Act & Assert
        try {
            bookService.updateBook(1L, updateRequestDto);
        } catch (ResourceNotFoundException e) {
            assertThat(e.getMessage()).contains("Book not found");
        }
    }

    @Test
    public void deleteBookTest() {
        // Arrange
        given(bookRepository.existsById(1L)).willReturn(true);

        // Act
        bookService.deleteBook(1L);

        // Assert
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deleteBook_NotFoundTest() {
        // Arrange
        given(bookRepository.existsById(1L)).willReturn(false);

        // Act & Assert
        try {
            bookService.deleteBook(1L);
        } catch (ResourceNotFoundException e) {
            assertThat(e.getMessage()).contains("Book not found");
        }
    }
}

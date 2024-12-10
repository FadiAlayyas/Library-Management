package com.example.library.management.service;

import com.example.library.management.dto.book.BookRequestDto;
import com.example.library.management.dto.book.BookResponseDto;
import com.example.library.management.exception.ResourceNotFoundException;
import com.example.library.management.model.Book;
import com.example.library.management.repository.BookRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Caching all books: Cache the list under the key 'all_books'
    @Cacheable(value = "books", key = "'all_books'")
    public List<BookResponseDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    // Caching an individual book by ID
    @Cacheable(value = "books", key = "#id")
    public BookResponseDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        return mapToResponseDto(book);
    }

    // Adding a book, and evicting all books cache to ensure it's updated
    @Transactional // Ensure data integrity during the book addition process
    @CachePut(value = "books", key = "#result.id")  // Cache the new book
    @CacheEvict(value = "books", key = "'all_books'")  // Evict all books cache
    public BookResponseDto addBook(BookRequestDto bookRequestDto) {
        Book book = mapToEntity(bookRequestDto);
        Book savedBook = bookRepository.save(book);
        return mapToResponseDto(savedBook);
    }

    // Updating an existing book, and updating cache accordingly
    @Transactional // Ensure data integrity during the book update process
    @CachePut(value = "books", key = "#id")  // Cache the updated book
    @CacheEvict(value = "books", key = "'all_books'")  // Evict all books cache
    public BookResponseDto updateBook(Long id, BookRequestDto dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        // Update book details
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPublicationYear(dto.getPublicationYear());
        book.setIsbn(dto.getIsbn());
        book.setPublisher(dto.getPublisher());
        book.setLanguage(dto.getLanguage());
        book.setTotalPages(dto.getTotalPages());
        book.setGenre(dto.getGenre());
        book.setAddedDate(dto.getAddedDate());
        book.setIsAvailable(dto.getIsAvailable());
        book.setDescription(dto.getDescription());

        Book updatedBook = bookRepository.save(book);
        return mapToResponseDto(updatedBook);
    }

    // Deleting a book, and evicting the cache for all books
    @Transactional // Ensure data integrity during the book deletion process
    @CacheEvict(value = "books", key = "'all_books'")  // Evict all books cache
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found");
        }
        bookRepository.deleteById(id);
    }

    // Helper method to map BookRequestDto to Book entity
    private Book mapToEntity(BookRequestDto dto) {
        return new Book(
                null,
                dto.getTitle(),
                dto.getAuthor(),
                dto.getPublicationYear(),
                dto.getIsbn(),
                dto.getPublisher(),
                dto.getLanguage(),
                dto.getTotalPages(),
                dto.getGenre(),
                dto.getAddedDate(),
                dto.getIsAvailable(),
                dto.getDescription()
        );
    }

    // Helper method to map Book entity to BookResponseDto
    private BookResponseDto mapToResponseDto(Book book) {
        return new BookResponseDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublicationYear(),
                book.getIsbn(),
                book.getPublisher(),
                book.getLanguage(),
                book.getTotalPages(),
                book.getGenre(),
                book.getAddedDate(),
                book.getIsAvailable(),
                book.getDescription()
        );
    }
}

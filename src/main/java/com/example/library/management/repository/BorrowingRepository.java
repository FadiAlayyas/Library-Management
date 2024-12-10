package com.example.library.management.repository;

import com.example.library.management.model.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {

    Optional<Borrowing> findByBookIdAndPatronIdAndReturnDateIsNull(Long bookId, Long patronId);
}

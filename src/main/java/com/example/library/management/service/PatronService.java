package com.example.library.management.service;

import com.example.library.management.dto.patron.PatronRequestDto;
import com.example.library.management.dto.patron.PatronResponseDto;
import com.example.library.management.model.Patron;
import com.example.library.management.repository.PatronRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatronService {

    private final PatronRepository patronRepository;

    // Constructor-based injection is a good practice for dependencies
    public PatronService(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
    }

    // Method for getting all patrons, marked as readOnly transaction
    @Transactional(readOnly = true)
    public List<PatronResponseDto> getAllPatrons() {
        return patronRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    // Method for getting a patron by ID, also readOnly
    @Transactional(readOnly = true)
    public PatronResponseDto getPatronById(Long id) {
        Patron patron = patronRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patron not found with ID: " + id));
        return convertToResponseDto(patron);
    }

    // Method to add a new patron, wrapped in a transaction
    @Transactional
    public PatronResponseDto addPatron(PatronRequestDto patronRequestDto) {
        Patron patron = convertToEntity(patronRequestDto);
        Patron savedPatron = patronRepository.save(patron); // Saving new patron
        return convertToResponseDto(savedPatron);
    }

    // Method to update an existing patron, wrapped in a transaction
    @Transactional
    public PatronResponseDto updatePatron(Long id, PatronRequestDto patronRequestDto) {
        Patron existingPatron = patronRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patron not found with ID: " + id));

        // Updating patron fields with data from the request DTO
        existingPatron.setName(patronRequestDto.getName());
        existingPatron.setEmail(patronRequestDto.getEmail());
        existingPatron.setPhone(patronRequestDto.getPhone());

        Patron updatedPatron = patronRepository.save(existingPatron); // Saving updated patron
        return convertToResponseDto(updatedPatron);
    }

    // Method to delete a patron, wrapped in a transaction
    @Transactional
    public void deletePatron(Long id) {
        if (!patronRepository.existsById(id)) {
            throw new EntityNotFoundException("Patron not found with ID: " + id);
        }
        patronRepository.deleteById(id); // Deleting the patron
    }

    // Helper method to convert a Patron entity to a PatronResponseDto
    private PatronResponseDto convertToResponseDto(Patron patron) {
        return new PatronResponseDto(
                patron.getId(),
                patron.getName(),
                patron.getEmail(),
                patron.getPhone()
        );
    }

    // Helper method to convert a PatronRequestDto to a Patron entity
    private Patron convertToEntity(PatronRequestDto patronRequestDto) {
        return new Patron(
                patronRequestDto.getId(),
                patronRequestDto.getName(),
                patronRequestDto.getEmail(),
                patronRequestDto.getPhone()
        );
    }
}

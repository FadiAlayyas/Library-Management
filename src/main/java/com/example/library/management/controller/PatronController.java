package com.example.library.management.controller;

import com.example.library.management.dto.patron.PatronRequestDto;
import com.example.library.management.dto.patron.PatronResponseDto;
import com.example.library.management.service.PatronService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patrons")
public class PatronController {

    private final PatronService patronService;

    public PatronController(PatronService patronService) {
        this.patronService = patronService;
    }

    @GetMapping
    public ResponseEntity<List<PatronResponseDto>> getAllPatrons() {
        List<PatronResponseDto> patrons = patronService.getAllPatrons();
        return ResponseEntity.ok(patrons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatronResponseDto> getPatronById(@PathVariable Long id) {
        PatronResponseDto patron = patronService.getPatronById(id);
        return ResponseEntity.ok(patron);
    }

    @PostMapping
    public ResponseEntity<PatronResponseDto> addPatron(@Validated @RequestBody PatronRequestDto patronRequestDto) {
        PatronResponseDto createdPatron = patronService.addPatron(patronRequestDto);
        return new ResponseEntity<>(createdPatron, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatronResponseDto> updatePatron(
            @PathVariable Long id,
            @Validated @RequestBody PatronRequestDto patronRequestDto) {
        PatronResponseDto updatedPatron = patronService.updatePatron(id, patronRequestDto);
        return ResponseEntity.ok(updatedPatron);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatron(@PathVariable Long id) {
        patronService.deletePatron(id);
        return ResponseEntity.noContent().build();
    }
}
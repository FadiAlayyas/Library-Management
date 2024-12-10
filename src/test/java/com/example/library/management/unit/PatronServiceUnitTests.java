package com.example.library.management.unit;

import com.example.library.management.dto.patron.PatronRequestDto;
import com.example.library.management.dto.patron.PatronResponseDto;
import com.example.library.management.model.Patron;
import com.example.library.management.repository.PatronRepository;
import com.example.library.management.service.PatronService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PatronServiceUnitTests {

    @Mock
    private PatronRepository patronRepository;
    @InjectMocks
    private PatronService patronService;
    private Patron patron;
    private PatronRequestDto patronRequestDto;
    private PatronResponseDto patronResponseDto;

    @BeforeEach
    public void setup() {
        // Setting up a sample patron
        patron = new Patron(1L, "John Doe", "john.doe@gmail.com", "1234567890");

        patronRequestDto = new PatronRequestDto("John Doe", "john.doe@gmail.com", "1234567890");

        patronResponseDto = new PatronResponseDto(1L, "John Doe", "john.doe@gmail.com", "1234567890");
    }

    @Test
    @Order(1)
    public void getAllPatronsTest() {
        // Arrange
        given(patronRepository.findAll()).willReturn(List.of(patron));

        // Act
        List<PatronResponseDto> patronList = patronService.getAllPatrons();

        // Assert
        assertThat(patronList).isNotNull();
        assertThat(patronList.size()).isEqualTo(1);
        assertThat(patronList.get(0).getName()).isEqualTo("John Doe");
    }

    @Test
    @Order(1)
    public void getPatronByIdTest() {
        // Arrange
        given(patronRepository.findById(1L)).willReturn(Optional.of(patron));

        // Act
        PatronResponseDto foundPatron = patronService.getPatronById(1L);

        // Assert
        assertThat(foundPatron).isNotNull();
        assertThat(foundPatron.getId()).isEqualTo(1L);
        assertThat(foundPatron.getName()).isEqualTo("John Doe");
    }

    @Test
    @Order(1)
    public void getPatronById_NotFoundTest() {
        // Arrange
        given(patronRepository.findById(1L)).willReturn(Optional.empty());

        // Act & Assert
        try {
            patronService.getPatronById(1L);
        } catch (EntityNotFoundException e) {
            assertThat(e.getMessage()).contains("Patron not found with ID: 1");
        }
    }

    @Test
    @Order(1)
    public void deletePatronTest() {
        // Arrange
        given(patronRepository.existsById(1L)).willReturn(true);

        // Act
        patronService.deletePatron(1L);

        // Assert
        verify(patronRepository, times(1)).deleteById(1L);
    }

    @Test
    @Order(1)
    public void deletePatron_NotFoundTest() {
        // Arrange
        given(patronRepository.existsById(1L)).willReturn(false);

        // Act & Assert
        try {
            patronService.deletePatron(1L);
        } catch (EntityNotFoundException e) {
            assertThat(e.getMessage()).contains("Patron not found with ID: 1");
        }
    }

    @Test
    @Order(1)
    public void createPatronTest() {

        Patron patronWithId = new Patron(1L, "John Doe", "john.doe@gmail.com", "1234567890");
        given(patronRepository.save(new Patron(null, "John Doe", "john.doe@gmail.com", "1234567890"))).willReturn(patronWithId);

        PatronResponseDto createdPatron = patronService.addPatron(patronRequestDto);

        assertThat(createdPatron).isNotNull();
        assertThat(createdPatron.getId()).isEqualTo(1L);
        assertThat(createdPatron.getName()).isEqualTo("John Doe");
        assertThat(createdPatron.getEmail()).isEqualTo("john.doe@gmail.com");
        assertThat(createdPatron.getPhone()).isEqualTo("1234567890");
    }

    @Test
    @Order(1)
    public void updatePatronTest() {
        // Arrange
        Patron existingPatron = new Patron(1L, "John Doe", "john.doe@gmail.com", "1234567890");
        PatronRequestDto patronRequestDto = new PatronRequestDto("John Updated", "john.updated@gmail.com", "0987654321");

        // Mocking the repository behavior for finding the patron by ID and saving it
        given(patronRepository.findById(1L)).willReturn(Optional.of(existingPatron));
        given(patronRepository.save(existingPatron)).willReturn(existingPatron);

        // Act
        PatronResponseDto updatedPatron = patronService.updatePatron(1L, patronRequestDto);

        // Assert
        assertThat(updatedPatron).isNotNull();
        assertThat(updatedPatron.getId()).isEqualTo(1L);
        assertThat(updatedPatron.getName()).isEqualTo("John Updated");
        assertThat(updatedPatron.getEmail()).isEqualTo("john.updated@gmail.com");
        assertThat(updatedPatron.getPhone()).isEqualTo("0987654321");

        // Verify that the save method was called once
        verify(patronRepository, times(1)).save(existingPatron);
    }

    @Test
    @Order(2)
    public void updatePatron_NotFoundTest() {
        // Arrange
        PatronRequestDto patronRequestDto = new PatronRequestDto("John Updated", "john.updated@gmail.com", "0987654321");

        // Mocking the repository to return empty when looking for a patron by ID
        given(patronRepository.findById(1L)).willReturn(Optional.empty());

        // Act & Assert
        try {
            patronService.updatePatron(1L, patronRequestDto);
        } catch (EntityNotFoundException e) {
            assertThat(e.getMessage()).contains("Patron not found with ID: 1");
        }
    }

}

package com.example.xts.patient;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    @Test
    public void testFindPatient_Success() {
        Long patientId = 1L;
        Patient mockPatient = new Patient();
        mockPatient.setId(patientId);
        mockPatient.setFirstName("John");
        mockPatient.setLastName("Doe");

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(mockPatient));

        Patient result = patientService.findPatient(patientId);
        assertNotNull(result);
        assertEquals(patientId, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
    }

    @Test
    public void testFindPatient_NotFound() {
        Long patientId = 1L;
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> patientService.findPatient(patientId));
    }

    @Test
    public void testGetAllPatients(){
        List<Patient> mockPatients = new ArrayList<>();
        mockPatients.add(new Patient("John", "Doe", LocalDate.of(1990, 1, 1), "Male", 1234567890, null));
        mockPatients.add(new Patient("Jane", "Smith", LocalDate.of(1995, 5, 10), "Female", 987654321, null));

        // Mock Page object
        Page<Patient> page = new PageImpl<>(mockPatients);

        // Mock repository method
        when(patientRepository.findAll(any(org.springframework.data.domain.Pageable.class))).thenReturn(page);

        // Call the service method
        Page<Patient> result = patientService.getPatients(1, 10, "asc", "asc");

        // Verify the result
        assertEquals(mockPatients.size(), result.getSize());
        assertEquals(mockPatients, result.getContent());
    }
    
    @Test
    public void testCheckPatientName_Exists(){
         when(patientRepository.findByFirstNameAndLastName("John", "Doe"))
         .thenReturn(Optional.of(new Patient()));

        //Verify that IllegalStateException is thrown
        assertThrows(IllegalStateException.class, () -> {
            patientService.checkPatientByName("John", "Doe");
        });

    }

    @Test
    public void testCheckPatientByName_NotExist() {
        when(patientRepository.findByFirstNameAndLastName("Jane", "Smith"))
                .thenReturn(Optional.empty());

        // No exception should be thrown
        assertDoesNotThrow(() -> {
            patientService.checkPatientByName("Jane", "Smith");
        });
    }

    @Test
    public void testAddPatient_PatientNotExist(){
        //return an empty Optional (patient does not exist)
        when(patientRepository.findByFirstNameAndLastName("John", "Doe"))
                .thenReturn(Optional.empty());

        when(patientRepository.save(any(Patient.class))).thenReturn(new Patient());

        // No exception should be thrown
        assertDoesNotThrow(() -> {
            patientService.addPatient(new Patient("John", "Doe", LocalDate.of(1990, 1, 1), "Male", 1234567890, null));
        });

        // Verify that patientRepository.save() is called once
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    public void testAddPatient_PatientExists() {
        //return a non-empty Optional (patient already exists)
        when(patientRepository.findByFirstNameAndLastName("John", "Doe"))
                .thenReturn(Optional.of(new Patient()));

        // Assertions: Verify that IllegalStateException is thrown
        assertThrows(IllegalStateException.class, () -> {
            patientService.addPatient(new Patient("John", "Doe", LocalDate.of(1990, 1, 1), "Male", 1234567890, null));
        });

        // Verify that patientRepository.save() is not called
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    public void testDeletePatient_PatientExists() {
        //return true (patient exists)
        when(patientRepository.existsById(1L)).thenReturn(true);

        // No exception should be thrown
        assertDoesNotThrow(() -> {
            patientService.deletePatient(1L);
        });

        // Verify that patientRepository.deleteById() is called once with the correct patient ID
        verify(patientRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeletePatient_PatientDoesNotExist() {
        // return false (patient does not exist)
        when(patientRepository.existsById(2L)).thenReturn(false);

        // Verify that IllegalStateException is thrown
        assertThrows(IllegalStateException.class, () -> {
            patientService.deletePatient(2L);
        });

        // Verify that patientRepository.deleteById() is not called
        verify(patientRepository, never()).deleteById(2L);
    }
    @Test
    public void testUpdatePatient_PatientExists() {
        // Mock existing patient
        Patient existingPatient = new Patient();
        existingPatient.setId(1L);
        existingPatient.setFirstName("John");
        existingPatient.setLastName("Doe");

        // Mock updated patient
        Patient updatedPatient = new Patient();
        updatedPatient.setFirstName("Jane");
        updatedPatient.setPhoneNumber(1234567890);

        // Mock repository behavior  to return the existing patient when findById is called
        when(patientRepository.findById(1L)).thenReturn(Optional.of(existingPatient));

        // Call the updatePatient method
        patientService.updatePatient(1L, updatedPatient);

        // Verify that patientRepository.save() is called with the updated patient
        verify(patientRepository, times(1)).save(existingPatient);

        // Verify that the existing patient is updated with the new values
        assertEquals("Jane", existingPatient.getFirstName());
        assertEquals("Doe", existingPatient.getLastName()); // Ensure last name remains unchanged
        assertEquals(1234567890, existingPatient.getPhoneNumber());
    }

    @Test
    public void testUpdatePatient_PatientNotFound() {
        // Mock repository behavior to return empty Optional when findById is called
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        // Assertions: Verify that IllegalArgumentException is thrown
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            patientService.updatePatient(1L, new Patient());
        });
        assertEquals("Patient with ID 1 not found", exception.getMessage());

        // Verify that patientRepository.save() is not called
        verify(patientRepository, never()).save(any());
    }
}

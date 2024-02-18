package com.example.xts.patient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@ExtendWith(MockitoExtension.class)


public class PatientControllerTest {
    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    private Patient patient;

    @Test
    public void testGetPatients() {
        // Create a single mock patient
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("John");
        patient.setLastName("Doe");

        List<Patient> mockPatients = Collections.singletonList(patient);

        // Create a Page object with the mock patients
        Page<Patient> page = new PageImpl<>(mockPatients);

        when(patientService.getPatients(1,10,"asc","asc")).thenReturn(page);
        Page<Patient> result = patientController.getPatients(1,10,"asc","asc");

        // Verify the result
        assertEquals(mockPatients, result.getContent());
    }
    @Test
    public void testFindPatient_Exists() {
        when(patientService.findPatient(1L)).thenReturn(patient);

        ResponseEntity<Patient> response = patientController.findPatient(1L);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(patient, response.getBody());
    }
    @Test
    public void testFindPatient_PatientNotFound() {
        when(patientService.findPatient(2L)).thenThrow(new IllegalArgumentException());

        ResponseEntity<Patient> response = patientController.findPatient(2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    public void testFindPatient_InternalServerError() {
        when(patientService.findPatient(3L)).thenThrow(new RuntimeException());

        ResponseEntity<Patient> response = patientController.findPatient(3L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testAddPatient_Success() {
        doNothing().when(patientService).addPatient(any());

        // Call the controller method
        ResponseEntity<String> response = patientController.addPatient(new Patient());

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Patient Saved", response.getBody());
    }
    @Test
    public void testAddPatient_PatientExists() {
        //use this when it is a void method
        doThrow(new IllegalStateException("Patient Exists")).when(patientService).addPatient(any());

        ResponseEntity<String> response = patientController.addPatient(new Patient());

        // Verify the response
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Patient Exists", response.getBody());
    }
    @Test
    public void testDeletePatient() {
        doNothing().when(patientService).deletePatient(any());

        patientController.deletePatient(1L);

        // Verify that the service method is called once with the correct patient ID
        verify(patientService, times(1)).deletePatient(1L);
    }
    @Test
    public void testUpdatePatient() {
        doNothing().when(patientService).updatePatient(any(Long.class), any(Patient.class));
    
        // Call the controller method
        patientController.updatePatient(1L, new Patient());
    
        // Verify that the service method is called once with the correct patient ID and updated patient object
        verify(patientService, times(1)).updatePatient(eq(1L), any(Patient.class));
    }

}

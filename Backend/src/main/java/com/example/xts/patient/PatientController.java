package com.example.xts.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping(path = "/api/patient")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/patients")
    public Page<Patient> getPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        return patientService.getPatients(page, size, sort, direction);
    }
    @GetMapping(path="{patientId}")
    public ResponseEntity<Patient> findPatient(@PathVariable("patientId") Long patientId) {
        try {
            Patient patient = patientService.findPatient(patientId);
            return ResponseEntity.ok(patient);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping
    public ResponseEntity<String> addPatient(@RequestBody Patient patient) {
        try {
            patientService.addPatient(patient);
            return ResponseEntity.ok("Patient Saved");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Patient Exists");
        }
    }
    @DeleteMapping(path = "{patientId}")
    public void deletePatient(@PathVariable("patientId") Long id){
        patientService.deletePatient(id);
    }
 
    @PutMapping(path = "{patientId}")
    public void updatePatient(@PathVariable("patientId") Long patientId, @RequestBody Patient updatedPatient){
        patientService.updatePatient(patientId, updatedPatient);
    }
}


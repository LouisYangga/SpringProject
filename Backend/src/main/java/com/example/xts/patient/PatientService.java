package com.example.xts.patient;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }


    public Page<Patient> getPatients(int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);
        return patientRepository.findAll(pageable);
    }

    public Patient findPatient(Long patientId){
        Patient patient = patientRepository.findById(patientId).orElseThrow(()-> new IllegalArgumentException("Patient with ID "+ patientId + " not found"));
        return patient;
    }
    public void checkPatientByName(String firstName, String lastName){
        Optional<Patient> patientName = patientRepository.findByFirstNameAndLastName(firstName, lastName);
        if(patientName.isPresent()){
         throw new IllegalStateException("Patient Exists");
        }
    }
    public void addPatient(Patient patient){
        try {
            checkPatientByName(patient.getFirstName(), patient.getLastName());
            patientRepository.save(patient);
        } catch (IllegalStateException e) {
            throw e; 
        }
    }

    public void deletePatient(Long patientId){
        boolean exists = patientRepository.existsById(patientId);
        if(!exists){
            throw new IllegalStateException("patient with ID " + patientId + " does not exists");
        }
        patientRepository.deleteById(patientId);
    }

    public void updatePatient(Long patientId, Patient updatedPatient) {
        Optional<Patient> existingPatientOptional = patientRepository.findById(patientId);
        if (existingPatientOptional.isEmpty()) {
            throw new IllegalArgumentException("Patient with ID " + patientId + " not found");
        }

        Patient existingPatient = existingPatientOptional.get();
        String firstName = updatedPatient.getFirstName();
        String lastName = updatedPatient.getLastName();
        String gender = updatedPatient.getGender();
        LocalDate DOB  = updatedPatient.getDOB();
        int phoneNumber = updatedPatient.getPhoneNumber();
        Address address = updatedPatient.getAddress();

        if (firstName!= null && firstName.length()>0) {
            existingPatient.setFirstName(firstName);
        }
        if (lastName != null && lastName.length()>0) {
            existingPatient.setLastName(lastName);
        }
        if (gender != null && gender.length() > 0) {
            existingPatient.setGender(gender);
        }
        if (DOB != null) {
            existingPatient.setDOB(DOB);
        }
        if (phoneNumber != 0) {
            existingPatient.setPhoneNumber(phoneNumber);
        }
        if (address != null) {
            existingPatient.setAddress(address);
        }

        patientRepository.save(existingPatient);
    }
}


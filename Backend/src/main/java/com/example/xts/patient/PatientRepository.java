package com.example.xts.patient;

import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>{
    Optional<Patient> findByFirstNameAndLastName(String firstName, String lastName);
    List<Patient> findByFirstName(String firstName);
    Page<Patient> findAll(Pageable pageable);
}

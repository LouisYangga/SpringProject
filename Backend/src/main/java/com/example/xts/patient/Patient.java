package com.example.xts.patient;
import java.time.LocalDate;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
@NoArgsConstructor
@ToString

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private @Getter @Setter Long id;
    private @Getter @Setter String firstName;
    private @Getter @Setter String lastName;
    private @Getter @Setter LocalDate DOB;
    private @Getter @Setter String Gender;
    private @Getter @Setter int phoneNumber;
    @Embedded
    private @Getter @Setter Address address;
    public Patient(String firstName, String lastName, LocalDate dOB, String gender, int phoneNumber, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        DOB = dOB;
        Gender = gender;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

}

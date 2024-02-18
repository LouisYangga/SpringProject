package com.example.xts.patient;
import jakarta.persistence.Embeddable;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Embeddable
public class Address {
    private @Getter @Setter String address;
    private @Getter @Setter String suburb;
    private @Getter @Setter String state;
    private @Getter @Setter int postcode;
    
}

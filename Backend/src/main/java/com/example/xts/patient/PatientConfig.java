package com.example.xts.patient;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PatientConfig {

    @Bean
    CommandLineRunner commandLineRunner(PatientRepository repository){
        return args->{  
            Patient louis = 
                new Patient("louis", 
                "yangga",
                LocalDate.of(2000, Month.NOVEMBER, 1), 
                "Male",
                123123, 
                new Address("catherine st", "Gwynneville", "NSW", 2500));
            Patient jack = 
                new Patient("Jack", 
                "Jack",
                LocalDate.of(2001, Month.NOVEMBER, 11), 
                "Male",
                32123222, 
                new Address("Frances st", "Wollongong", "NSW", 2500));
            
            repository.saveAll(List.of(louis, jack));
        };
    }
}

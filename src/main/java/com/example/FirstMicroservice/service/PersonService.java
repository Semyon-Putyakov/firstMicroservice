package com.example.FirstMicroservice.service;

import com.example.FirstMicroservice.dto.PersonDTO;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
public class PersonService {

    private final


    public Optional<PersonDTO> getPersonDTO(String username) {

        PersonDTO personDTO = new PersonDTO.PersonDTOBuilder()
                .setUsername(username)
                .build();



    }
}

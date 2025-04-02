package com.example.FirstMicroservice.service;

import com.example.FirstMicroservice.dto.PersonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonServiceFromSecurity {
    private final PersonService personService;

    @Autowired
    public PersonServiceFromSecurity(PersonService personService) {
        this.personService = personService;
    }

    public Optional<PersonDTO> getUserByUsername(String username) throws InterruptedException {
        return personService.getPersonDTO(username); // Получаем пользователя из PersonService
    }

    public void createUser(PersonDTO personDTO) {
        personService.createPersonDTO(personDTO); // Создаем пользователя через PersonService
    }
}

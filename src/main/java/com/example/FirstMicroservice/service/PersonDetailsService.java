package com.example.FirstMicroservice.service;


import com.example.FirstMicroservice.dto.PersonDTO;
import com.example.FirstMicroservice.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final PersonService personService;

    @Autowired
    public PersonDetailsService(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<PersonDTO> personDTO;
        try {
            personDTO = personService.getPersonDTO(username);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (personDTO.isEmpty()) {
            throw new UsernameNotFoundException("Пользователь не найден");
        } else {
            return new PersonDetails(personDTO.get());
        }
    } // тут поток новый надо создать
}

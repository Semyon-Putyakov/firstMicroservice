package com.example.FirstMicroservice.validation;

import com.example.FirstMicroservice.dto.PersonDTO;
import com.example.FirstMicroservice.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {

    private final PersonService personService;

    @Autowired
    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return PersonDTO.class.equals(clazz);
    }


    @Override
    public void validate(Object target, Errors errors) {
        PersonDTO validatedPerson = (PersonDTO) target;

        try {
            PersonDTO personResponse = personService.getPersonDTO(validatedPerson.getUsername()).orElse(null);
            if (personResponse != null) {
                errors.rejectValue("username", "Такой пользователь уже существует");
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}

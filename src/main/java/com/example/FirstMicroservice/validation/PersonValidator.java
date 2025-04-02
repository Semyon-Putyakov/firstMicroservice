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

        PersonDTO personDTO = (PersonDTO) target;

        try {
            Optional<PersonDTO> person = personService.getPersonDTO(personDTO.getUsername());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(personDTO.getUsername() != null){
            errors.rejectValue("username", "Такой пользователь уже существует");
        }

    }
}

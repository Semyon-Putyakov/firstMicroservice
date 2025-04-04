package com.example.FirstMicroservice.validation;

import com.example.FirstMicroservice.model.PersonModel;
import com.example.FirstMicroservice.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {
    private final PersonService personService;

    @Autowired
    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return PersonModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PersonModel validatedPerson = (PersonModel) target;
        var personResponse = personService.getPersonDTO(validatedPerson.getUsername()).orElse(null);
        if (personResponse != null) {
            errors.rejectValue("username", null, "Такой пользователь уже существует");
        }
    }
}

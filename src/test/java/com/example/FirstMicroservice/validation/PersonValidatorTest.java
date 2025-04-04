package com.example.FirstMicroservice.validation;

import com.example.FirstMicroservice.dto.PersonDTO;
import com.example.FirstMicroservice.model.PersonModel;
import com.example.FirstMicroservice.service.PersonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PersonValidatorTest {

    @Mock
    private PersonService personService;

    private PersonValidator personValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        personValidator = new PersonValidator(personService);
    }

    @Test
    void validate_WhenUsernameExists_AddsError() {
        PersonModel personModel = new PersonModel("existingUser", "password", 1);
        PersonDTO existingPerson = new PersonDTO.PersonDTOBuilder()
                .setUsername("existingUser")
                .build();

        Mockito.when(personService.getPersonDTO("existingUser")).thenReturn(Optional.of(existingPerson));
        
        Errors errors = new BeanPropertyBindingResult(personModel, "personModel");

        personValidator.validate(personModel, errors);

        Assertions.assertTrue(errors.hasErrors());
        Assertions.assertEquals("Такой пользователь уже существует", errors.getFieldError("username").getDefaultMessage());
    }

    @Test
    void validate_WhenUsernameDoesNotExist_NoErrors() {
        PersonModel personModel = new PersonModel("newUser", "password", 1);
        Mockito.when(personService.getPersonDTO("newUser")).thenReturn(Optional.empty());
        Errors errors = new BeanPropertyBindingResult(personModel, "personModel");

        personValidator.validate(personModel, errors);

        Assertions.assertFalse(errors.hasErrors());
    }
} 
package com.example.FirstMicroservice.controller;

import com.example.FirstMicroservice.dto.PersonDTO;
import com.example.FirstMicroservice.model.PersonModel;
import com.example.FirstMicroservice.security.PersonDetails;
import com.example.FirstMicroservice.service.PersonService;
import com.example.FirstMicroservice.validation.PersonValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FirstMicroserviceControllerTest {

    @Mock
    private PersonService personService;

    @Mock
    private PersonValidator personValidator;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private FirstMicroserviceController controller;

    private PersonModel personModel;
    private PersonDTO personDTO;

    @BeforeEach
    void setUp() {
        personModel = new PersonModel();
        personModel.setId(1);
        personModel.setUsername("testuser");
        personModel.setPassword("password");

        personDTO = new PersonDTO();
        personDTO.setId(1);
        personDTO.setUsername("testuser");
        personDTO.setPassword("password");
    }

    @Test
    void registration_ShouldReturnRegistrationView() {
        String viewName = controller.registration(personModel);
        Assertions.assertEquals("auth/registration", viewName);
    }

    @Test
    void putPerson_WithValidData_ShouldRedirectToLogin() {
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        Mockito.when(modelMapper.map(personModel, PersonDTO.class)).thenReturn(personDTO);

        String viewName = controller.putPerson(personModel, bindingResult);

        Assertions.assertEquals("auth/login", viewName);
        Mockito.verify(personService).createPersonDTO(personDTO);
    }

    @Test
    void putPerson_WithInvalidData_ShouldReturnRegistrationView() {
        Mockito.when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = controller.putPerson(personModel, bindingResult);

        Assertions.assertEquals("auth/registration", viewName);
        Mockito.verify(personService, never()).createPersonDTO(any());
    }

    @Test
    void login_ShouldReturnLoginView() {
        String viewName = controller.login();
        Assertions.assertEquals("auth/login", viewName);
    }

    @Test
    void user_ShouldReturnPersonPageWithAuthenticatedUser() {
        PersonDetails personDetails = new PersonDetails(personDTO);
        Mockito.when(authentication.getPrincipal()).thenReturn(personDetails);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String viewName = controller.user(model);

        Assertions.assertEquals("afterPage/personPage", viewName);
        Mockito.verify(model).addAttribute("person", personDTO);
    }

    @Test
    void delete_ShouldRedirectToLogin() {
        String viewName = controller.delete(personModel);
        Assertions.assertEquals("redirect:/auth/login", viewName);
        Mockito.verify(personService).deletePersonDTO(personModel.getId());
    }

    @Test
    void updateGet_WithExistingId_ShouldReturnUpdateView() {
        Mockito.when(personService.getPersonById(1)).thenReturn(Optional.of(personDTO));
        Mockito.when(modelMapper.map(personDTO, PersonModel.class)).thenReturn(personModel);

        String viewName = controller.update(model, 1);

        Assertions.assertEquals("afterPage/update", viewName);
        Mockito.verify(model).addAttribute("person", personModel);
    }

    @Test
    void updatePatch_WithValidData_ShouldRedirectToLogin() {
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        Mockito.when(modelMapper.map(personModel, PersonDTO.class)).thenReturn(personDTO);

        String viewName = controller.update(personModel, bindingResult);

        Assertions.assertEquals("redirect:/auth/login", viewName);
        Mockito.verify(personService).updatePersonDTO(personDTO);
    }

    @Test
    void updatePatch_WithInvalidData_ShouldReturnUpdateView() {
        Mockito.when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = controller.update(personModel, bindingResult);

        Assertions.assertEquals("afterPage/update", viewName);
        Mockito.verify(personService, never()).updatePersonDTO(any());
    }
}
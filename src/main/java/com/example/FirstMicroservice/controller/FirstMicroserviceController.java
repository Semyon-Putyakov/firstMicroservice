package com.example.FirstMicroservice.controller;

import com.example.FirstMicroservice.dto.PersonDTO;
import com.example.FirstMicroservice.service.PersonService;
import com.example.FirstMicroservice.validation.PersonValidator;
import jakarta.validation.Valid;
import org.apache.kafka.common.network.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class FirstMicroserviceController {

    private final PersonService personService;
    private final PersonValidator personValidator;

    @Autowired
    public FirstMicroserviceController(PersonService personService, PersonValidator personValidator) {
        this.personService = personService;
        this.personValidator = personValidator;
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("person") PersonDTO personDTO) {

        return "auth/registration";
    }

    @PostMapping("/registration")
    public String putPerson(@ModelAttribute("person") @Valid PersonDTO personDTO, BindingResult bindingResult) {

        personValidator.validate(personDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }

        System.out.println("after create");
        personService.createPersonDTO(personDTO);
        return "auth/registration"; // return login
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
    // берем от сюда все это дело или не отсюда
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
//    personDetails.getPerson();
    // или не отсюда крч завтра решу и надо будет #fields error сделать валидацию крч

    //POST реализация логина с рестом и кафкой


    @GetMapping("/user")
    public String user() {
        return "afterPage/personPage";
    }

    
}

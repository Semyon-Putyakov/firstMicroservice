package com.example.FirstMicroservice.controller;

import com.example.FirstMicroservice.dto.PersonDTO;
import com.example.FirstMicroservice.security.PersonDetails;
import com.example.FirstMicroservice.service.PersonService;
import com.example.FirstMicroservice.validation.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        personService.createPersonDTO(personDTO);
        return "auth/login";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }


    @GetMapping("/user")
    public String user(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        PersonDTO personDTO = personDetails.getPerson();
        model.addAttribute("person", personDTO);
        return "afterPage/personPage";
    }

    @DeleteMapping("/delete")
    public String delete(@ModelAttribute("person") PersonDTO personDTO) {
        System.out.println(personDTO.toString());
        return "redirect:/auth/login";
    }

    @GetMapping("/update/{id}")
    public String update(Model model, @PathVariable("id") int id) {
        PersonDTO personDTO =  personService.getPersonById(id).orElse(null);
        System.out.println(personDTO.toString());
        model.addAttribute("person", personDTO);
        return "afterPage/update";
    }

    @PatchMapping("/update")
    public String update(@ModelAttribute("person") @Valid PersonDTO personDTO, BindingResult bindingResult) {

        System.out.println("persondto: " + personDTO.toString());

        personValidator.validate(personDTO, bindingResult);
        System.out.println("valid");
        if (bindingResult.hasErrors()) {
            System.out.println("if");
            return "afterPage/update";
        }
        System.out.println("method");
        personService.updatePersonDTO(personDTO);
        System.out.println("updated person");
        return "redirect:/auth/login";
    }
}

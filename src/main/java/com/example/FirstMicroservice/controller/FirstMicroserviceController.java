package com.example.FirstMicroservice.controller;

import com.example.FirstMicroservice.dto.PersonDTO;
import com.example.FirstMicroservice.model.PersonModel;
import com.example.FirstMicroservice.security.PersonDetails;
import com.example.FirstMicroservice.service.PersonService;
import com.example.FirstMicroservice.validation.PersonValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @Autowired
    public FirstMicroserviceController(PersonService personService, PersonValidator personValidator, ModelMapper modelMapper) {
        this.personService = personService;
        this.personValidator = personValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("person") PersonModel personModel) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String putPerson(@ModelAttribute("person") @Valid PersonModel personModel, BindingResult bindingResult) {
        personValidator.validate(personModel, bindingResult);
        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }
        PersonDTO personDTO = modelMapper.map(personModel, PersonDTO.class);
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
    public String delete(@ModelAttribute("person") PersonModel personModel) {
        personService.deletePersonDTO(personModel.getId());
        return "redirect:/auth/login";
    }

    @GetMapping("/update/{id}")
    public String update(Model model, @PathVariable("id") int id) {
        PersonDTO personDTO = personService.getPersonById(id).orElse(null);
        PersonModel personModel = modelMapper.map(personDTO, PersonModel.class);
        model.addAttribute("person", personModel);
        return "afterPage/update";
    }

    @PatchMapping("/update")
    public String update(@ModelAttribute("person") @Valid PersonModel personModel, BindingResult bindingResult) {
        personValidator.validate(personModel, bindingResult);
        if (bindingResult.hasErrors()) {
            return "afterPage/update";
        }
        PersonDTO personDTO = modelMapper.map(personModel, PersonDTO.class);
        personService.updatePersonDTO(personDTO);
        return "redirect:/auth/login";
    }
}

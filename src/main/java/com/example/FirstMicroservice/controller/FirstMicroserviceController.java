package com.example.FirstMicroservice.controller;

import com.example.FirstMicroservice.security.PersonDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class FirstMicroserviceController {


    @GetMapping("/registration")
    public String registration() {
        return "auth/registration";
    }

    //POST реализация регистрации с рестом и кафкой

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

package com.example.FirstMicroservice.controller;

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

    //POST реализация логина с рестом и кафкой


    @GetMapping("/user")
    public String user() {
        return "afterPage/personPage";
    }

    
}

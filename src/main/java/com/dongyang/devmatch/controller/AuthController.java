package com.dongyang.devmatch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginForm() {
        return "auth/login";
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "auth/signup"; //
    }
}

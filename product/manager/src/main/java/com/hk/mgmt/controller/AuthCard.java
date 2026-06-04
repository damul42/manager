package com.hk.mgmt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth-card")
public class AuthCard {

@GetMapping("/new-pass")
public String newPass() {
    return "auth-card/new-pass";
}
    

@GetMapping("/login-pin")
public String loginPin() {
    return "auth-card/login-pin";
}
    

@GetMapping("/lock-screen")
public String lockScreen() {
    return "auth-card/lock-screen";
}
    

@GetMapping("/two-factor")
public String twoFactor() {
    return "auth-card/two-factor";
}
    

@GetMapping("/delete-account")
public String deleteAccount() {
    return "auth-card/delete-account";
}
    

@GetMapping("/success-mail")
public String successMail() {
    return "auth-card/success-mail";
}
    

@GetMapping("/reset-pass")
public String resetPass() {
    return "auth-card/reset-pass";
}
    

@GetMapping("/sign-up")
public String signUp() {
    return "auth-card/sign-up";
}
    

@GetMapping("/sign-in")
public String signIn() {
    return "auth-card/sign-in";
}
    
}
    
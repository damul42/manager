package com.hk.mgmt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class Auth {

@GetMapping("/new-pass")
public String newPass() {
    return "auth/new-pass";
}
    

@GetMapping("/login-pin")
public String loginPin() {
    return "auth/login-pin";
}
    

@GetMapping("/lock-screen")
public String lockScreen() {
    return "auth/lock-screen";
}
    

@GetMapping("/two-factor")
public String twoFactor() {
    return "auth/two-factor";
}
    

@GetMapping("/delete-account")
public String deleteAccount() {
    return "auth/delete-account";
}
    

@GetMapping("/success-mail")
public String successMail() {
    return "auth/success-mail";
}
    

@GetMapping("/reset-pass")
public String resetPass() {
    return "auth/reset-pass";
}
    

@GetMapping("/sign-up")
public String signUp() {
    return "auth/sign-up";
}
    

@GetMapping("/sign-in")
public String signIn() {
    return "auth/sign-in";
}
    
}
    
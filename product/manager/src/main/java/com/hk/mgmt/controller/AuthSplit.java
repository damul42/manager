package com.hk.mgmt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth-split")
public class AuthSplit {

@GetMapping("/new-pass")
public String newPass() {
    return "auth-split/new-pass";
}
    

@GetMapping("/login-pin")
public String loginPin() {
    return "auth-split/login-pin";
}
    

@GetMapping("/lock-screen")
public String lockScreen() {
    return "auth-split/lock-screen";
}
    

@GetMapping("/two-factor")
public String twoFactor() {
    return "auth-split/two-factor";
}
    

@GetMapping("/delete-account")
public String deleteAccount() {
    return "auth-split/delete-account";
}
    

@GetMapping("/success-mail")
public String successMail() {
    return "auth-split/success-mail";
}
    

@GetMapping("/reset-pass")
public String resetPass() {
    return "auth-split/reset-pass";
}
    

@GetMapping("/sign-up")
public String signUp() {
    return "auth-split/sign-up";
}
    

@GetMapping("/sign-in")
public String signIn() {
    return "auth-split/sign-in";
}
    
}
    
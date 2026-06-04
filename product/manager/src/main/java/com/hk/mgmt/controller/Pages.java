package com.hk.mgmt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pages")
public class Pages {

@GetMapping("/account-settings")
public String accountSettings() {
    return "pages/account-settings";
}
    

@GetMapping("/coming-soon")
public String comingSoon() {
    return "pages/coming-soon";
}
    

@GetMapping("/profile")
public String profile() {
    return "pages/profile";
}
    

@GetMapping("/gallery")
public String gallery() {
    return "pages/gallery";
}
    

@GetMapping("/terms-conditions")
public String termsConditions() {
    return "pages/terms-conditions";
}
    

@GetMapping("/sitemap")
public String sitemap() {
    return "pages/sitemap";
}
    

@GetMapping("/faq")
public String faq() {
    return "pages/faq";
}
    

@GetMapping("/pricing")
public String pricing() {
    return "pages/pricing";
}
    

@GetMapping("/timeline")
public String timeline() {
    return "pages/timeline";
}
    

@GetMapping("/search-results")
public String searchResults() {
    return "pages/search-results";
}
    

@GetMapping("/empty")
public String empty() {
    return "pages/empty";
}
    

@GetMapping("/privacy-policy")
public String privacyPolicy() {
    return "pages/privacy-policy";
}
    
}
    
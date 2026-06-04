package com.hk.mgmt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/form")
public class Form {

@GetMapping("/select")
public String select() {
    return "form/select";
}
    

@GetMapping("/wizard")
public String wizard() {
    return "form/wizard";
}
    

@GetMapping("/other-plugin")
public String otherPlugin() {
    return "form/other-plugin";
}
    

@GetMapping("/validation")
public String validation() {
    return "form/validation";
}
    

@GetMapping("/layout")
public String layout() {
    return "form/layout";
}
    

@GetMapping("/elements")
public String elements() {
    return "form/elements";
}
    

@GetMapping("/pickers")
public String pickers() {
    return "form/pickers";
}
    

@GetMapping("/range-slider")
public String rangeSlider() {
    return "form/range-slider";
}
    

@GetMapping("/text-editors")
public String textEditors() {
    return "form/text-editors";
}
    

@GetMapping("/fileuploads")
public String fileuploads() {
    return "form/fileuploads";
}
    
}
    
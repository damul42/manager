package com.hk.mgmt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class Error {

@GetMapping("/500")
public String error500() {
    return "error/500";
}
    

@GetMapping("/403")
public String error403() {
    return "error/403";
}
    

@GetMapping("/400")
public String error400() {
    return "error/400";
}
    

@GetMapping("/408")
public String error408() {
    return "error/408";
}
    

@GetMapping("/401")
public String error401() {
    return "error/401";
}
    

@GetMapping("/maintenance")
public String maintenance() {
    return "error/maintenance";
}
    

@GetMapping("/404")
public String error404() {
    return "error/404";
}
    
}
    
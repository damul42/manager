package com.hk.mgmt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboards")
public class Dashboard {

@GetMapping("/analytics")
public String analytics() {
    return "dashboard/analytics";
}
    

@GetMapping("/ecommerce")
public String ecommerce() {
    return "dashboard/ecommerce";
}
    
}
    
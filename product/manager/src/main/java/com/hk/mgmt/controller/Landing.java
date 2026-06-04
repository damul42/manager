package com.hk.mgmt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/landing")
public class Landing {

@GetMapping("/index")
public String index() {
    return "landing/index";
}
    
}
    
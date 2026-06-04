package com.hk.mgmt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/metrics")
public class Metrics {

@GetMapping("/index")
public String index() {
    return "metrics/index";
}
    
}
    
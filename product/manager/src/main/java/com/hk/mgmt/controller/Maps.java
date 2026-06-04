package com.hk.mgmt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/maps")
public class Maps {

@GetMapping("/leaflet")
public String leaflet() {
    return "maps/leaflet";
}
    

@GetMapping("/google")
public String google() {
    return "maps/google";
}
    

@GetMapping("/vector")
public String vector() {
    return "maps/vector";
}
    
}
    
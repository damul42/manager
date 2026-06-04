package com.hk.mgmt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/layouts")
public class Layouts {

@GetMapping("/preloader")
public String preloader() {
    return "layouts/preloader";
}
    

@GetMapping("/boxed")
public String boxed() {
    return "layouts/boxed";
}
    

@GetMapping("/horizontal")
public String horizontal() {
    return "layouts/horizontal";
}
    

@GetMapping("/scrollable")
public String scrollable() {
    return "layouts/scrollable";
}
    

@GetMapping("/compact")
public String compact() {
    return "layouts/compact";
}
    

@GetMapping("/sidebar/with-lines")
public String sidebarWithLines() {
    return "layouts/sidebar/with-lines";
}
    

@GetMapping("/sidebar/on-hover")
public String sidebarOnHover() {
    return "layouts/sidebar/on-hover";
}
    

@GetMapping("/sidebar/no-icons")
public String sidebarNoIcons() {
    return "layouts/sidebar/no-icons";
}
    

@GetMapping("/sidebar/gray")
public String sidebarGray() {
    return "layouts/sidebar/gray";
}
    

@GetMapping("/sidebar/image")
public String sidebarImage() {
    return "layouts/sidebar/image";
}
    

@GetMapping("/sidebar/offcanvas")
public String sidebarOffcanvas() {
    return "layouts/sidebar/offcanvas";
}
    

@GetMapping("/sidebar/gradient")
public String sidebarGradient() {
    return "layouts/sidebar/gradient";
}
    

@GetMapping("/sidebar/on-hover-active")
public String sidebarOnHoverActive() {
    return "layouts/sidebar/on-hover-active";
}
    

@GetMapping("/sidebar/compact")
public String sidebarCompact() {
    return "layouts/sidebar/compact";
}
    

@GetMapping("/sidebar/light")
public String sidebarLight() {
    return "layouts/sidebar/light";
}
    

@GetMapping("/topbar/gray")
public String topbarGray() {
    return "layouts/topbar/gray";
}
    

@GetMapping("/topbar/gradient")
public String topbarGradient() {
    return "layouts/topbar/gradient";
}
    

@GetMapping("/topbar/dark")
public String topbarDark() {
    return "layouts/topbar/dark";
}
    
}
    
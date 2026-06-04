package com.hk.mgmt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui")
public class Ui {

@GetMapping("/tabs")
public String tabs() {
    return "ui/tabs";
}
    

@GetMapping("/colors")
public String colors() {
    return "ui/colors";
}
    

@GetMapping("/breadcrumb")
public String breadcrumb() {
    return "ui/breadcrumb";
}
    

@GetMapping("/grid")
public String grid() {
    return "ui/grid";
}
    

@GetMapping("/pagination")
public String pagination() {
    return "ui/pagination";
}
    

@GetMapping("/accordions")
public String accordions() {
    return "ui/accordions";
}
    

@GetMapping("/scrollspy")
public String scrollspy() {
    return "ui/scrollspy";
}
    

@GetMapping("/links")
public String links() {
    return "ui/links";
}
    

@GetMapping("/dropdowns")
public String dropdowns() {
    return "ui/dropdowns";
}
    

@GetMapping("/notifications")
public String notifications() {
    return "ui/notifications";
}
    

@GetMapping("/carousel")
public String carousel() {
    return "ui/carousel";
}
    

@GetMapping("/badges")
public String badges() {
    return "ui/badges";
}
    

@GetMapping("/videos")
public String videos() {
    return "ui/videos";
}
    

@GetMapping("/utilities")
public String utilities() {
    return "ui/utilities";
}
    

@GetMapping("/offcanvas")
public String offcanvas() {
    return "ui/offcanvas";
}
    

@GetMapping("/placeholders")
public String placeholders() {
    return "ui/placeholders";
}
    

@GetMapping("/typography")
public String typography() {
    return "ui/typography";
}
    

@GetMapping("/list-group")
public String listGroup() {
    return "ui/list-group";
}
    

@GetMapping("/modals")
public String modals() {
    return "ui/modals";
}
    

@GetMapping("/cards")
public String cards() {
    return "ui/cards";
}
    

@GetMapping("/spinners")
public String spinners() {
    return "ui/spinners";
}
    

@GetMapping("/popovers")
public String popovers() {
    return "ui/popovers";
}
    

@GetMapping("/alerts")
public String alerts() {
    return "ui/alerts";
}
    

@GetMapping("/collapse")
public String collapse() {
    return "ui/collapse";
}
    

@GetMapping("/progress")
public String progress() {
    return "ui/progress";
}
    

@GetMapping("/tooltips")
public String tooltips() {
    return "ui/tooltips";
}
    

@GetMapping("/images")
public String images() {
    return "ui/images";
}
    

@GetMapping("/buttons")
public String buttons() {
    return "ui/buttons";
}
    
}
    
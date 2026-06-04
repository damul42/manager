package com.hk.mgmt.controller.pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardPageController {

    @GetMapping("")
    public String index() {
        return "redirect:/dashboard/analytics";
    }

    @GetMapping("/analytics")
    public String analytics() {
        return "dashboard/analytics";
    }

    @GetMapping("/ecommerce")
    public String ecommerce() {
        return "dashboard/ecommerce";
    }
}
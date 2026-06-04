package com.hk.mgmt.controller.pages;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeePageController {

    @GetMapping("/list")
    public String list() {
        return "pages/employee/list";
    }

    @GetMapping("/create")
    public String create() {
        return "pages/employee/create";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable String id, Model model) {
        model.addAttribute("employeeId", id);
        return "pages/employee/view";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable String id, Model model) {
        model.addAttribute("employeeId", id);
        return "pages/employee/edit";
    }
}
package com.hk.mgmt.controller.pages;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserPageController {

    @GetMapping("/list")
    public String list() {
        return "pages/user/list";
    }

    @GetMapping("/create")
    public String create() {
        return "pages/user/create";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable String id, Model model) {
        model.addAttribute("userId", id);
        return "pages/user/detail";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable String id, Model model) {
        model.addAttribute("userId", id);
        return "pages/user/edit";
    }
}
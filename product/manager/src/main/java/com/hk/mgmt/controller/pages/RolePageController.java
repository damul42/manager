package com.hk.mgmt.controller.pages;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/role")
@RequiredArgsConstructor
public class RolePageController {

    @GetMapping("/list")
    public String list() {
        return "pages/role/list";
    }

    @GetMapping("/create")
    public String create() {
        return "pages/role/create";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable String id, Model model) {
        model.addAttribute("roleId", id);
        return "pages/role/view";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable String id, Model model) {
        model.addAttribute("roleId", id);
        return "pages/role/edit";
    }
}
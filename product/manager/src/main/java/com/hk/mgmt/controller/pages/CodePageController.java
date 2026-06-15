package com.hk.mgmt.controller.pages;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/code")
@RequiredArgsConstructor
public class CodePageController {

    @GetMapping("/list")
    public String list() {
        return "pages/code/list";
    }
}
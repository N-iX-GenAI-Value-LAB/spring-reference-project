package com.nix.reference.spring.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/workflow")
    public String workflow() {
        return "redirect:/workflow.html";
    }
}

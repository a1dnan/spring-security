package com.adn4n.springsecurity.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web")
public class webController {

    @GetMapping()
    public String test(){
        return "Hello from secured endpoint";
    }
}

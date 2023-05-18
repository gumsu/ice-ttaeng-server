package com.example.teamtwelvebackend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Slf4j
public class TestController {
    @GetMapping("/hello")
    public String hello(Principal principal) {
        log.info(principal.toString());
        return "Hello";
    }
}

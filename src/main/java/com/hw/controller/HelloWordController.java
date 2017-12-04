package com.hw.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by HeWei on 2017/12/4.
 * .
 */
@RestController
public class HelloWordController {

    @GetMapping(value = "/hello/{name}")
    public ResponseEntity index(@PathVariable String name) {
        return ResponseEntity.ok().body("hello " + name);
    }
}

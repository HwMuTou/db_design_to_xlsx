package com.hw.controller;

import com.hw.domain.DBProperty;
import com.hw.repository.DBPropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by HeWei on 2017/11/29.
 * .
 */
@RestController
@RequestMapping(value = "/dbProperty", produces = "application/json; charset=utf-8")
public class DBPropertyController {

    private DBPropertyRepository dbPropertyRepository;

    @Autowired
    public DBPropertyController(DBPropertyRepository dbPropertyRepository) {
        this.dbPropertyRepository = dbPropertyRepository;
    }

    @GetMapping
    public ResponseEntity index() {
        return ResponseEntity.ok(dbPropertyRepository.findAll());
    }

    @PostMapping
    public ResponseEntity create(@RequestBody DBProperty dbProperty) {
        return ResponseEntity.ok(dbPropertyRepository.save(dbProperty));
    }
}

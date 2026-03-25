package com.safetyNet.controller;

import com.safetyNet.model.Person;
import com.safetyNet.services.PersonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/safety")
public class DataHandlerController {

    @Autowired
    private final PersonServices personServices;

    public DataHandlerController(PersonServices personServices) {
        this.personServices = personServices;
    }

    @GetMapping("/persons")
    public List<Person> getAllPersons() {
        return personServices.getAllPersons();
    }





    }






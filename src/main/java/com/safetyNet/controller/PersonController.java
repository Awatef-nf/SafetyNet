package com.safetyNet.controller;

import com.safetyNet.model.Person;
import com.safetyNet.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class PersonController {

    @Autowired
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/persons")
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }


    @GetMapping("/communityEmail")
    public List<String> getByCity(@RequestParam(name = "city") String city) {
        return personService.getEmailsByCity(city);
    }
}











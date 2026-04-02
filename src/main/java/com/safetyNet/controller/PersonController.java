package com.safetyNet.controller;

import com.safetyNet.model.Person;
import com.safetyNet.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PostMapping("/person")
    public Person addPerson(@RequestBody Person person){ return personService.addPerson(person);}

    @GetMapping("/communityEmail")
    public List<String> getByCity(@RequestParam(name = "city") String city) {
        return personService.getEmailsByCity(city);
    }

    @GetMapping("/firestation")
    public Map<String, Object> getPersonsByStation(@RequestParam(name = "stationNumber") String station){
        return personService.getPersonsByStation(station);
    }

    @GetMapping("/personInfo")
    public List<Map<String,Object>> getpersonInfo(@RequestParam String firstName, @RequestParam String lastName){
        return personService.getPersonsInfo(firstName,lastName);
    }

}











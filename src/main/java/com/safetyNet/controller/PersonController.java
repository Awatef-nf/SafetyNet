package com.safetyNet.controller;

import com.safetyNet.model.Person;
import com.safetyNet.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/person/add")
    public Person addPerson(@RequestBody Person person){ return personService.addPerson(person);}

    @PutMapping("/person/update")
    public ResponseEntity<Person> updatePerson(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestBody Person person) {

        Person updated = personService.updatePerson(firstName, lastName, person);

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }

//    @DeleteMapping("person/delete")
//    public void deletePerson(@RequestParam String firstName, @RequestParam String lastName){
//        personService.deletePerson(firstName, lastName);
//    }

    @DeleteMapping("/person/delete")
    public ResponseEntity<String> deletePerson(
            @RequestParam String firstName,
            @RequestParam String lastName) {

        boolean deleted = personService.deletePerson(firstName, lastName);

        if (!deleted) {
            return ResponseEntity.status(404).body("Person not found");
        }

        return ResponseEntity.ok("Person deleted successfully");
    }

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









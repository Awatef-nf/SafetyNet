package com.safetyNet.services;

import com.safetyNet.model.Person;
import com.safetyNet.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

   //tous les personnes
    public List<Person> getAllPersons() {
        return personRepository.findAllPersons();
    }

   //Chercher les personnes par leurs city
    public List<String> getEmailsByCity(String city) {
        List<String> emails = new ArrayList<>();
        List<Person> persons = personRepository.findAllPersons();

        for (Person person : persons) {
            if(person.getCity().equals(city))
            {
                emails.add(person.getEmail());
            }
        }

        return emails;
    }
}

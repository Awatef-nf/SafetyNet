package com.safetyNet.services;

import com.safetyNet.model.Firestation;
import com.safetyNet.model.Medicalrecord;
import com.safetyNet.model.Person;
import com.safetyNet.repository.DataHandler;
import com.safetyNet.repository.FireStationRepository;
import com.safetyNet.repository.MedicalrecordRepository;
import com.safetyNet.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;
    private MedicalrecordRepository medicalrecordRepository;
    private FireStationRepository fireStationRepository;
    private DataHandler dataHandler;

    public PersonService(MedicalrecordRepository medicalrecordRepository, FireStationRepository fireStationRepository, DataHandler dataHandler) {
        this.medicalrecordRepository = medicalrecordRepository;
        this.fireStationRepository = fireStationRepository;
        this.dataHandler = dataHandler;
    }

//===================================================================================================================//
//++++++++++++++++++++++++++++++++++++++++  LES ENDPOINTS +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    //ajouter une personne
    public Person addPerson(Person person){
    dataHandler.getData().getPersons().add(person);
    return person;
}
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    //modifier une personne
    public Person modifyPerson(String firstName, String lastName, Person updatedPerson){
        for(Person person : dataHandler.getData().getPersons()){
            if(person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)){
                person.setAddress(updatedPerson.getAddress());
                person.setPhone(updatedPerson.getPhone());
            }
    }return updatedPerson;
    }

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
//tous les personnes
    public List<Person> getAllPersons() {
    return personRepository.findAllPersons();
}
//=========================================  LES URLS  ===============================================================//
    //calcule d'age
    public Integer getAge(Person person) {

        Medicalrecord medicalrecord = medicalrecordRepository.findMedicalRecord(person.getFirstName(), person.getLastName());
        if (medicalrecord == null || medicalrecord.getBirthdate() == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        try {
            LocalDate birthDate = LocalDate.parse(medicalrecord.getBirthdate(), formatter);
            return Period.between(birthDate, LocalDate.now()).getYears();
        } catch (DateTimeParseException e) {
            System.out.println("Date invalide pour " + person.getFirstName() + " " + person.getLastName());
            return null;
        }
    }
//===================================================================================================================//

    public List<Person> findPersonsByAddress(String address) {
        List<Person> persons = personRepository.findAllPersons();
        List<Person> result = new ArrayList<>();

        for (Person person : persons) {
            if (person.getAddress().equals(address)) {
                Medicalrecord medicalrecord = medicalrecordRepository.findMedicalRecord(
                        person.getFirstName(), person.getLastName());
                if (medicalrecord != null) { // on inclut seulement ceux qui ont un medicalrecord
                    result.add(person);
                }
            }
        }
        return result;
    }
//===================================================================================================================//
    public List<String> getEmailsByCity(String city) {
        List<String> emails = new ArrayList<>();
        List<Person> persons = personRepository.findAllPersons();

        for (Person person : persons) {
            if (person.getCity().equals(city)) {
                emails.add(person.getEmail());
            }
        }
    return emails;
    }

//===================================================================================================================//
    //version retourner une liste de personnes deservit par une caserne et decompter le nombre d'adulte et enfants
    public Map<String, Object> getPersonsByStation(String station) {
        List<Firestation> firestations = fireStationRepository.findAllStation();

        List<Map<String, String>> personsAtStation = new ArrayList<>();
        int adultCount = 0;
        int childCount = 0;

        for (Firestation firestation : firestations) {
            if (firestation.getStation().equals(station)) {
                List<Person> personsAtAddress = findPersonsByAddress(firestation.getAddress());
                for (Person person : personsAtAddress) {
                    Integer age = getAge(person);
                    if (age != null) {
                        if (age >= 18) adultCount++;
                        else childCount++;
                        Map<String, String> p = new HashMap<>();
                        p.put("firstName", person.getFirstName());
                        p.put("lastName", person.getLastName());
                        p.put("address", person.getAddress());
                        p.put("phone", person.getPhone());
                        personsAtStation.add(p);
                    }
                }
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("persons", personsAtStation);
        result.put("adultCount", adultCount);
        result.put("childCount", childCount);
        return result;
    }
//===================================================================================================================//
public List<Map<String, Object>> getPersonsInfo(String firstName, String lastName) {
    List<Person> persons = personRepository.findAllPersons();
    List<Map<String, Object>> result = new ArrayList<>();

    // Trouver la personne
    Map<String, Object> personByName = new HashMap<>();
    List<String> members = new ArrayList<>();
    //je cherche les info de la personne
    for (Person person : persons) {
        if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
            Medicalrecord medicalrecords = medicalrecordRepository.findMedicalRecord(firstName, lastName);
            personByName.put("firstName", person.getFirstName());
            personByName.put("address", person.getAddress());
            personByName.put("age", getAge(person));
            personByName.put("email", person.getEmail());
            personByName.put("MedicalRecord", medicalrecords.getMedications());
        } else if(person.getLastName().equals(lastName)) {
        // Membres du même foyer
        members.add( person.getFirstName());
        }
        }
            if (!members.isEmpty()) { personByName.put("members", members);}
            if (!personByName.isEmpty()) { result.add(personByName);}
        return result;
        }
}
//===================================================================================================================//








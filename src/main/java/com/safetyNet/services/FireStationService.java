package com.safetyNet.services;

import com.safetyNet.model.Firestation;
import com.safetyNet.model.Medicalrecord;
import com.safetyNet.model.Person;
import com.safetyNet.repository.FireStationRepository;
import com.safetyNet.repository.MedicalrecordRepository;
import com.safetyNet.repository.PersonRepository;

import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FireStationService {

    @Autowired
    private FireStationRepository fireStationRepository;
    private final PersonRepository personRepository;
    private final MedicalrecordRepository medicalrecordRepository;

    private final PersonService personService;

    public FireStationService(FireStationRepository fireStationRepository, PersonRepository personRepository, MedicalrecordRepository medicalrecordRepository, PersonService personService) {
        this.fireStationRepository = fireStationRepository;
        this.personRepository = personRepository;
        this.medicalrecordRepository = medicalrecordRepository;
        this.personService = personService;
    }
//===================================================================================================================//
    // Chercher liste des numero telphone par station
    public List<String> getPhonesByStation(String station) {
        List<String> phones = new ArrayList<>();
        List<Person> persons = personRepository.findAllPersons();
        List<Firestation> firestations = fireStationRepository.findAllStation();

        for (Firestation firestation : firestations) {
            for (Person person : persons) {
                if (firestation.getStation().equals(station)) {
                    if (firestation.getAddress().equals(person.getAddress())) {
                        if (!phones.contains(person.getPhone())) { // pour eviter les doublants: s il existe deja tu le rajoute pas
                            phones.add(person.getPhone());
                        }
                    }
                }
            }
        }
    return phones;
    }
//===================================================================================================================//
    //version stream: Chercher liste des numero telphone par station
    public List<String> getPhonesByStationByStream(String station) {
        List<Person> persons = personRepository.findAllPersons();
        List<Firestation> firestations = fireStationRepository.findAllStation();

        return firestations.stream()
                .filter(firestation -> firestation.getStation().equals(station)) //filtrer par rapport à la station
                .flatMap(firestation -> persons.stream()//je marcours persons
                        .filter(person -> firestation.getAddress().equals(person.getAddress())) //je filter par l'adresse
                )
                .map(Person::getPhone)//je recupere les numero de telephone
                .distinct() //j'évite les doublants
                .toList(); //je transforme le resultat final en une liste
    }

//===================================================================================================================//
    //chercher une liste d'enfants par une adresse ainsi leurs membres  sinon une liste vide
    public Map<String, Object> getChild(String address) {
            List<Person> persons = personService.findPersonsByAddress(address);
            Medicalrecord r = null;
            List<Map<String, Object>> children = new ArrayList<>();
            List<Map<String, String>> members = new ArrayList<>();
            for (Person person : persons) {
                        Integer age = personService.getAge(person);
                        if (age != null) {
                            if (age < 18) {
                                Map<String, Object> child = new HashMap<>();
                                child.put("firstName", person.getFirstName());
                                child.put("lastName", person.getLastName());
                                child.put("age", age);
                                children.add(child);
                            } else {
                                Map<String, String> member = new HashMap<>();
                                member.put("firstName", person.getFirstName());
                                member.put("lastName", person.getLastName());
                                members.add(member);
                            }
                        }
                    }
            Map<String, Object> result = new HashMap<>(); //je stocke le resutat dans une map
            result.put("children", children);
            result.put("Members", members);
    return result;
    }

//===================================================================================================================//
    //liste des habitans correspendants à l'adresse donnée ainsi leurs age et antécedants medicaux
    public List<Map<String, Object>> getResidentByAdressAndMedicalrecord(String address) {
            List<Person> persons = personService.findPersonsByAddress(address);
            List<Map<String, Object>> result = new ArrayList<>();

            for (Person p : persons) {
                if (address.equals(p.getAddress())) {
                    Medicalrecord record = medicalrecordRepository.findMedicalRecord(p.getFirstName(), p.getLastName());
                    if (record != null) {
                        Map<String, Object> personRecord = new HashMap<>();
                        personRecord.put("firstName", p.getFirstName());
                        personRecord.put("lastName", p.getLastName());
                        personRecord.put("phone", p.getPhone());

                        // Calcul de l'âge
                        Integer age = personService.getAge(p);
                        personRecord.put("Age", age);

                        personRecord.put("Allergies", record.getAllergies());
                        personRecord.put("Medications", record.getMedications());

                        result.add(personRecord);
                    }
                }
            }
    return result;
    }
//===================================================================================================================//
    //retournée liste des foyers deservient par une liste de station, cette liste doit etre regouper par adress et chaque foyer: firstName,phone,age
    //et antécédants medicaux
    public Map<String, List<Map<String, String>>> getPersonsByStationsGroupedByAddress(List<String> stations) {
        Map<String, List<Map<String, String>>> personsByAddress = new HashMap<>();
        for (String station : stations) {
            Map<String, Object> personsByStation = personService.getPersonsByStation(station);
            List<Map<String, String>> personsAtStation = (List<Map<String, String>>) personsByStation.get("persons");

            // Regrouper par adresse
            for (Map<String, String> p : personsAtStation) {
                String address = p.get("address");
                if (!personsByAddress.containsKey(address)) {//si l'address n'existe pas en va renvoyer une liste vide
                    personsByAddress.put(address, new ArrayList<>());
                }
                personsByAddress.get(address).add(p);
            }
        }            return personsByAddress;
    }
}



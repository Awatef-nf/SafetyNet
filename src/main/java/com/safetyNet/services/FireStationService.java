package com.safetyNet.services;

import com.safetyNet.model.Firestation;
import com.safetyNet.model.Medicalrecord;
import com.safetyNet.model.Person;
import com.safetyNet.repository.FireStationRepository;
import com.safetyNet.repository.MedicalrecordRepository;
import com.safetyNet.repository.PersonRepository;
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

    public FireStationService(FireStationRepository fireStationRepository, PersonRepository personRepository, MedicalrecordRepository medicalrecordRepository) {
        this.fireStationRepository = fireStationRepository;
        this.personRepository = personRepository;
        this.medicalrecordRepository = medicalrecordRepository;
    }

    // Chercher liste des numero telphone par station
    public List<String> getEPhonesByStation(String station) {
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

    //version stream: Chercher liste des numero telphone par station
    public List<String> getEPhonesByStationByStream(String station) {
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


    //version retourner une liste de personnes deservit par une caserne et decompter le nombre d'adulte et enfants

        public Map<String, Object> getPersonsByStation(String station) {

            List<Person> persons = personRepository.findAllPersons();
            List<Firestation> firestations = fireStationRepository.findAllStation();

            //une maps dans une liste pour recuperer plusieurs données
            List<List<String>> personsAtStation = new ArrayList<>();

            // pour compter le nombre d'adulte et de jeune
            int adultCount = 0;
            int childCount = 0;


            for (Firestation firestation : firestations) {
                if (firestation.getStation().equals(station)) {
                    for (Person person : persons) {
                        if (person.getAddress().equals(firestation.getAddress())) {
                            //je compare la personne dans Person à celle dans medicalrecord
                            Medicalrecord medicalrecord = medicalrecordRepository.findMedicalRecord(person.getFirstName(), person.getLastName());
                            if (medicalrecord != null && medicalrecord.getBirthdate() != null) {
                                //je formate la date puisque birtdate est sous format string
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                try {
                                    //je crée lE BON format de date pour continuer le calcule
                                    LocalDate birthDate = LocalDate.parse(medicalrecord.getBirthdate(), formatter);
                                    int age = calculateAge(birthDate);
                                    if (age >= 18) adultCount++;
                                    else childCount++;
                                    // je crée une Map avec seulement les infos voulues
                                    List<String> p = new ArrayList<>();
                                    p.add(person.getFirstName());
                                    p.add(person.getLastName());
                                    p.add(person.getAddress());
                                    p.add(person.getPhone());
                                    personsAtStation.add(p);

                                } catch (DateTimeParseException e) {
                                    System.out.println("Date invalide pour " + person.getFirstName() + " " + person.getLastName());
                                }
                            }
                        }
                    }
                }
            }

            Map<String, Object> result = new HashMap<>(); //la map est plus lisible et plus fluide en récuperation
            result.put("persons", personsAtStation);
            result.put("adultCount", adultCount);
            result.put("childCount", childCount);

            return result;
        }
        // Méthode utilitaire pour calculer l'âge
        private int calculateAge(LocalDate birthDate) {
            return Period.between(birthDate, LocalDate.now()).getYears();
        }
    }



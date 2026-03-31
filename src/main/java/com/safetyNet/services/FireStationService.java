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


    //version retourner une liste de personnes deservit par une caserne et decompter le nombre d'adulte et enfants

        public Map<String, Object> getPersonsByStation(String station) {

            List<Person> persons = personRepository.findAllPersons();
            List<Firestation> firestations = fireStationRepository.findAllStation();

            //une map dans une liste pour recuperer plusieurs données
            List<Map<String,String>> personsAtStation = new ArrayList<>();

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
                                    Map<String,String> p = new HashMap<>();
                                    p.put("first name",person.getFirstName());
                                    p.put("last name",person.getLastName());
                                    p.put("adress",person.getAddress());
                                    p.put("phone",person.getPhone());
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

//    public List<String> infoStationNumber(String station) {     //Méthode qui retourne une liste de String
//
//        List<Person> areaPerson = fireStationRepository.findAllStation().stream()  // on récupére en premier toute les firestations pour les transformer en stream
//                .filter(firestation -> firestation.getStation().equals(station))    //on filtre, si station est égal à celle demandée on la garde
//                .flatMap(firestation -> {   //Idem qu'une boucle for, mais là on récupére toutes les données pour les mettres à plat dans une seule liste
//                    String firestationAddress = firestation.getAddress();   //on stock l'address dans une variable
//                    return personRepository.findAllPersons().stream()   //on récupére toutes les personnes
//                            .filter(person -> person.getAddress().equals(firestationAddress));  //si l'address de la personne est égal à la station on la garde
//                })
//                .collect(Collectors.toList());  //on récupére tout et on le met dans une liste et on la renvoit
//        long adults = areaPerson.stream()
//                .filter(person -> {     // on filtre dans person
//                    return medicalrecordRepository.findAllMedical().stream() //on recupere tout medicalrecord
//                            .filter(medicalrecord -> medicalrecord.getFirstName().equals(person.getFirstName()) //on filtre par nom et prenom egal de medicalrecord et person
//                                    && medicalrecord.getLastName().equals(person.getLastName()))
//                            .findFirst()    //on ne cherche que le premier egal
//                            .map(me(medicalrecord.getBirthdate()) >= 18) //on garde que ceux egal et superieur a 18
//                            .orElse(false);
//                })
//                .count();   //on comptabilise
//
//        long children = areaPerson.size() - adults;
//
//        List<String> infos = areaPerson.stream()
//                .map(person -> person.getFirstName() + " " + person.getLastName() + " " + person.getAddress() + " " + person.getPhone())
//                .collect(Collectors.toList());
//
//        // ✅ Ajoute le compte
//        infos.add("Nombres d'adultes : " + adults);
//        infos.add("Nombres d'enfants : " + children);
//
//        return infos;
//    }



        // Méthode utilitaire pour calculer l'âge
        private int calculateAge(LocalDate birthDate) {
            return Period.between(birthDate, LocalDate.now()).getYears();
        }



        //chercher une liste d'enfants par une adresse ainsi leurs membres  sinon une liste vide
        public Map<String, Object> getChild(String address) {
            List<Person> persons = personRepository.findAllPersons();
            List<Medicalrecord> records = medicalrecordRepository.findAllMedical();

            List<Map<String, Object>> children = new ArrayList<>();
            List<Map<String, String>> members = new ArrayList<>();

            for (Person p : persons) {
                if (p.getAddress().equals(address)) {

                    Medicalrecord medicalrecord = null; //le cas echant elle retourne une liste vide

                    for (Medicalrecord record : records) {
                        if (record.getFirstName().equals(p.getFirstName()) &&
                                record.getLastName().equals(p.getLastName())) {
                            medicalrecord = record;
                            break;
                        } //je m'assure que je suis sur la mm personne
                    }
                    if (medicalrecord != null) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                        LocalDate birthDate = LocalDate.parse(medicalrecord.getBirthdate(), formatter);
                        int age = calculateAge(birthDate);
                        //si je trouve la bonne personne j calcule l'age
                        if (age <= 18) {
                            //je crée une map our mettre dedans toutes les informations
                            Map<String, Object> child = new HashMap<>();
                            child.put("firstName", p.getFirstName());
                            child.put("lastName", p.getLastName());
                            child.put("age", age);
                            children.add(child);
                        } else { // s'il est adulte je le met avec les membres
                            Map<String, String> member = new HashMap<>();
                            member.put("firstName", p.getFirstName());
                            member.put("lastName", p.getLastName());
                            members.add(member);
                        }
                    }
                }
            }
            //je stocke le resutat dans une map
            Map<String, Object> result = new HashMap<>();
            result.put("children", children);
            result.put("Members", members);

            return result;
        }



        //liste des habitans correspendants à l'adresse donnée ainsi leurs age et antécedants medicaux
        public List<Map<String, Object>> getResidentByAdressAndMedicalrecord(String address) {

            List<Person> persons = personRepository.findAllPersons();
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
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                        LocalDate birthDate = LocalDate.parse(record.getBirthdate(), formatter);
                        int age = calculateAge(birthDate);
                        personRecord.put("Age", age);

                        personRecord.put("Allergies", record.getAllergies());
                        personRecord.put("Medications", record.getMedications());

                        result.add(personRecord);
                    }

                }
            }

            return result;
        }

}



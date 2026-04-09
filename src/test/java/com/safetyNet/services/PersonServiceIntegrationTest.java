package com.safetyNet.services;

import com.safetyNet.model.Medicalrecord;
import com.safetyNet.model.Person;
import com.safetyNet.repository.DataHandler;
import com.safetyNet.repository.MedicalrecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PersonServiceIntegrationTest {

    @Autowired
    private PersonService personService;

    @Autowired
    private MedicalrecordRepository medicalrecordRepository;

    @Autowired
    private DataHandler dataHandler;

    @BeforeEach
    void setup() {
        // Nettoyer toutes les données avant chaque test
        dataHandler.getData().getPersons().clear();
        dataHandler.getData().getMedicalrecords().clear();
    }

//    @Test
//    void findPersonsByAddress_retourneDesPersonnes_quandAdresseExiste() {
//        // Ajouter une personne avec l'adresse
//        Person person = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
//        dataHandler.getData().getPersons().add(person);
//
//        List<Person> result = personService.findPersonsByAddress("1509 Culver St");
//
//        assertThat(result).isNotEmpty()
//                .allMatch(p -> p.getAddress().equals("1509 Culver St"));
//    }

    @Test
    void findPersonsByAddress_retourneListeVide_quandAdresseInexistante() {
        List<Person> result = personService.findPersonsByAddress("Adresse Inconnue");

        assertThat(result).isEmpty();
    }

    @Test
    void findPersonsByAddress_retournePersonnesAvecMedicalRecord() {
        // Ajouter personne et son MedicalRecord
        Person person = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        dataHandler.getData().getPersons().add(person);

        Medicalrecord record = new Medicalrecord("John", "Boyd", "03/06/1984", List.of("allergie").toArray(new String[0]), List.of("medicament").toArray(new String[0]));
        dataHandler.getData().getMedicalrecords().add(record);

        // Appel du service pour récupérer les personnes par adresse
        List<Person> result = personService.findPersonsByAddress("1509 Culver St");

        // Vérifie que la liste n’est pas vide
        assertThat(result).isNotEmpty();

        // Vérifie que chaque personne a un MedicalRecord via le repository
        result.forEach(p -> {
            Medicalrecord rec = medicalrecordRepository.findMedicalRecord(p.getFirstName(), p.getLastName());
            assertThat(rec).isNotNull();
        });
    }

    @Test
    void findPersonsByAddress_exclutPersonnesSansMedicalRecord() {
        // Ajouter deux personnes, une avec et une sans MedicalRecord
        Person withRecord = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        Person withoutRecord = new Person("Jane", "Doe", "1509 Culver St", "Culver", "97451", "841-874-0000", "janedoe@email.com");
        dataHandler.getData().getPersons().add(withRecord);
        dataHandler.getData().getPersons().add(withoutRecord);

        // Ajouter MedicalRecord pour John seulement
        Medicalrecord record = new Medicalrecord("John", "Boyd", "03/06/1984", List.of("allergie").toArray(new String[0]), List.of("medicament").toArray(new String[0]));
        dataHandler.getData().getMedicalrecords().add(record);

        List<Person> result = personService.findPersonsByAddress("1509 Culver St");

        // Vérifie que chaque personne retournée a un MedicalRecord
        result.forEach(p -> {
            Medicalrecord rec = medicalrecordRepository.findMedicalRecord(p.getFirstName(), p.getLastName());
            assertThat(rec).isNotNull();
        });
    }
}
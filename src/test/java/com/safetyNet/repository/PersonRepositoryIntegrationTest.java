package com.safetyNet.repository;

import com.safetyNet.model.Person;
import com.safetyNet.services.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PersonRepositoryIntegrationTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private DataHandler dataHandler;
    @Test
    void testfindAllPersons() {

                List<Person> persons =dataHandler.getData().getPersons();
                assertNotNull(persons);
                assertFalse(persons.isEmpty());
            }
        }




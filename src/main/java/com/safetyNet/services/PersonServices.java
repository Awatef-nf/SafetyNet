package com.safetyNet.services;

import com.safetyNet.model.Person;
import com.safetyNet.repository.DataHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServices {
    @Autowired
    private final DataHandler dataHandler;

    // Spring injecte DataHandler automatiquement
    public PersonServices(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    public List<Person> getAllPersons() {
        return dataHandler.getPersons();
    }
}
package com.safetyNet.repository;

import com.safetyNet.model.Firestation;
import com.safetyNet.model.Person;
import com.safetyNet.services.FireStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class FireStationRepository {

    @Autowired
    private DataHandler dataHandler;

    // retourner toutes les stations
    public List<Firestation> findAllStation() {
        return dataHandler.getData().getFirestations();
    }
}

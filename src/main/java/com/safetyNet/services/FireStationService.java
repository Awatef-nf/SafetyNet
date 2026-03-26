package com.safetyNet.services;

import com.safetyNet.model.Firestation;
import com.safetyNet.model.Person;
import com.safetyNet.repository.FireStationRepository;
import com.safetyNet.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FireStationService {

    @Autowired
    private FireStationRepository fireStationRepository;
    private PersonRepository personRepository ;


    public FireStationService(FireStationRepository fireStationRepository, PersonRepository personRepository) {
        this.fireStationRepository = fireStationRepository;
        this.personRepository = personRepository;
    }

    //Chercher liste des numero telphone par station
    public List<String> getEPhonesByStation(String station ){
        List<String> phones= new ArrayList<>();

        List<Person> persons =personRepository.findAllPersons();
        List<Firestation> firestations = fireStationRepository.findAllStation();

        for (Firestation firestation : firestations) {
            for(Person person : persons)
            {
                if(firestation.getStation().equals(station))
                {
                    if(firestation.getAddress().equals(person.getAddress()))
                    {
                        phones.add(person.getPhone());
                    }
                }
            }

        } return  phones;
    }

}

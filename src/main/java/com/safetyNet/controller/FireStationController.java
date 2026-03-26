package com.safetyNet.controller;

import com.safetyNet.repository.FireStationRepository;
import com.safetyNet.services.FireStationService;
import com.safetyNet.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FireStationController {


    @Autowired
    private FireStationService fireStationService;

    @GetMapping("/phoneAlert")
    public List<String> getPhoneNumberer(@RequestParam(name = "station") String station)
    {
        return fireStationService.getEPhonesByStation(station);
    }
}




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
import java.util.Map;

@RestController
public class FireStationController {


    @Autowired
    private FireStationService fireStationService;

    @GetMapping("/phoneAlert")
    public List<String> getPhoneNumber(@RequestParam(name = "station") String station)
    {
        return fireStationService.getEPhonesByStation(station);
    }


    @GetMapping("/phoneAlert/stream")
    public List<String> getPhoneNumberByStream(@RequestParam(name = "station") String station)
    {
        return fireStationService.getEPhonesByStationByStream(station);
    }


    @GetMapping("/firestation")
    public Map<String, Object> getPersonsByStation(@RequestParam(name = "stationNumber") String station){
        return fireStationService.getPersonsByStation(station);
    }
}




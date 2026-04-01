package com.safetyNet.controller;

import com.safetyNet.services.FireStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class FireStationController {


    @Autowired
    private FireStationService fireStationService;

    @GetMapping("/phoneAlert")
    public List<String> getPhoneNumber(@RequestParam(name = "station") String station)
    {
        return fireStationService.getPhonesByStation(station);
    }

    @GetMapping("/phoneAlert/stream")
    public List<String> getPhoneNumberByStream(@RequestParam(name = "station") String station)
    {
        return fireStationService.getPhonesByStationByStream(station);
    }

    @GetMapping("/childAlert")
    public Map<String, Object> getChild(@RequestParam(name = "address") String address){
        return fireStationService.getChild(address);
    }

    @GetMapping("/fire")
    public List<Map<String, Object>> getResident(@RequestParam(name = "address") String address){
        return fireStationService.getResidentByAdressAndMedicalrecord(address);
    }

    @GetMapping("flood/station")
    public Map<String, List<Map<String, String>>> getPersonsByStationsGroupedByAddress(@RequestParam(name="stations") String stationsToGet) {
        List<String> stations = Arrays.asList(stationsToGet.split(","));
        return fireStationService.getPersonsByStationsGroupedByAddress(stations);
    }

}




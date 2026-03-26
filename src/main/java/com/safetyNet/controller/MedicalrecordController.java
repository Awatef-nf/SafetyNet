package com.safetyNet.controller;

import com.safetyNet.model.Medicalrecord;
import com.safetyNet.model.Person;
import com.safetyNet.services.MedicalrecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MedicalrecordController {
    @Autowired
    private final MedicalrecordService medicalrecordService;


    public MedicalrecordController(MedicalrecordService medicalrecordService) {
        this.medicalrecordService = medicalrecordService;
    }


    @GetMapping("/medicalrecord")
    public List<Medicalrecord> getAllMedical() {
        return medicalrecordService.getAllMedicalrecord();
    }




}

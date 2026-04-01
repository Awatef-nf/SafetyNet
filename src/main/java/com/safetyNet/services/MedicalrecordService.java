package com.safetyNet.services;


import com.safetyNet.model.Medicalrecord;
import com.safetyNet.repository.MedicalrecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalrecordService {
    @Autowired
    private MedicalrecordRepository medicalrecordRepository;

    public List<Medicalrecord> getAllMedicalrecord(){
       return medicalrecordRepository.findAllMedical();
    }




}

package com.safetyNet.repository;

import com.safetyNet.model.Medicalrecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MedicalrecordRepository {

    private DataHandler dataHandler;

    public MedicalrecordRepository(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    public List<Medicalrecord> findAllMedical() {
        return dataHandler.getData().getMedicalrecords();
    }
}

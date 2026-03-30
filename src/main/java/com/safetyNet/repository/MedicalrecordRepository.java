package com.safetyNet.repository;

import com.safetyNet.model.Medicalrecord;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MedicalrecordRepository {

    private DataHandler dataHandler;

    public MedicalrecordRepository(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    public List<Medicalrecord> findAllMedical() {
        return dataHandler.getData().getMedicalrecords();
    }

    public Medicalrecord findMedicalRecord(String firstName, String lastName) {
        for (Medicalrecord record : findAllMedical()) {
            if (record.getFirstName().equals(firstName) &&
                    record.getLastName().equals(lastName)) {
                return record;
            }
        }
        return null;
    }

}

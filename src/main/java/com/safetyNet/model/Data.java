package com.safetyNet.model;

import java.util.List;

public class Data {

    private List<Person> persons;
    private List<Medicalrecord>  medicalrecords;
    private List<Firestation> firestations;


   //on a pas besoin du construteur et par defaut le consparDefaut il est y deajt avec l'objet

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public List<Medicalrecord> getMedicalrecords() {
        return medicalrecords;
    }

    public void setMedicalrecords(List<Medicalrecord> medicalrecords) {
        this.medicalrecords = medicalrecords;
    }

    public List<Firestation> getFirestations() {
        return firestations;
    }

    public void setFirestations(List<Firestation> firestations) {
        this.firestations = firestations;
    }
}

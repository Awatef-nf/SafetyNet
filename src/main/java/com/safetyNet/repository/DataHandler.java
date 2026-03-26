package com.safetyNet.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetyNet.model.Data;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class DataHandler {

    private Data data;

    public DataHandler() {
        loadDataFromJson();
    }
    //on charge le dataJson
    public void loadDataFromJson() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = getClass().getResourceAsStream("/data.json")) {
            if (is == null) {
                System.err.println("Fichier data.json introuvable dans le classpath !");
                return;
            }
            data = mapper.readValue(is, Data.class); //C'est la ligne qui désérialise ton JSON en objet Data
            System.out.println("JSON chargé !");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Data getData() {
        return data;
    }
}
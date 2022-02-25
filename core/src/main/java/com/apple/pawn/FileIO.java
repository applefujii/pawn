package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FileIO {

    private SaveData saveData;

    public boolean save() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(saveData);
            Gdx.app.debug("savaData", json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean load() {
        ObjectMapper mapper = new ObjectMapper();
//        SaveData t = mapper.readValue(json, SaveData.class);
        return true;
    }

    public void setSaveData(SaveData saveData) {
        this.saveData = saveData;
    }
}

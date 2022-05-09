package com.apple.pawn;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

/**
 * ファイルの読み書き
 * Jacksonを使用。
 * @author fujii
 */
public class FileIO {

    private SaveData saveData;

    public FileIO() {
        saveData = new SaveData();
    }

    public boolean save() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            mapper.writeValue(new File("./save/savedata1.sav"), saveData.cpy());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean load() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            saveData = mapper.readValue(new File("./save/savedata1.sav"), SaveData.class);
        } catch(IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void delete() {
        File file = new File("./save/savedata1.sav");
        file.delete();
    }

    public boolean isExistsSaveData() {
        File file = new File("./save/savedata1.sav");
        return file.exists();
    }

    public SaveData getSaveData() {
        return saveData;
    }

    public void setSaveData(SaveData saveData) {
        this.saveData = saveData;
    }
}

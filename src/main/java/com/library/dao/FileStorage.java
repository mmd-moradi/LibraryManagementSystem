package com.library.dao;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class FileStorage<T> {
    private final String storagePath;
    private final Class<T> type;
    private final Gson gson;

    public FileStorage(String filename, Class<T> type) {
        // Create data directory if it doesn't exist
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
        
        this.storagePath = "data/" + filename;
        this.type = type;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        
        // Initialize storage file if it doesn't exist
        initializeStorageFile();
    }
    
    private void initializeStorageFile() {
        try {
            File file = new File(storagePath);
            if (!file.exists()) {
                // Create a new file with empty array
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("[]");
                }
            }
        } catch (IOException e) {
            System.err.println("Error initializing storage file: " + e.getMessage());
        }
    }
    
    public void saveAll(List<T> items) {
        try (FileWriter writer = new FileWriter(storagePath)) {
            gson.toJson(items, writer);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }
    
    public List<T> loadAll() {
        try {
            if (!Files.exists(Paths.get(storagePath))) {
                return new ArrayList<>();
            }
            
            try (FileReader reader = new FileReader(storagePath)) {
                return gson.fromJson(reader, TypeToken.getParameterized(List.class, type).getType());
            }
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
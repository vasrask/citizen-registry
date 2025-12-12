package gr.aegean.rest.repositories;

import gr.aegean.domain.Citizen;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CitizenRepository {

    private static final String FILENAME = "citizens.dat";
    private static Map<String, Citizen> db = new HashMap<>();

    static {
        loadFromFile();
    }

    @SuppressWarnings("unchecked")
    private static void loadFromFile() {
        File file = new File(FILENAME);
        if (!file.exists()) {
            System.out.println("No existing DB file found, starting fresh.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            db = (Map<String, Citizen>) ois.readObject();
            System.out.println("DB loaded. Citizens: " + db.size());
        } catch (Exception e) {
            System.err.println("Failed to load DB: " + e.getMessage());
        }
    }

    private static void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            oos.writeObject(db);
        } catch (Exception e) {
            System.err.println("Failed to save DB: " + e.getMessage());
        }
        System.out.println("Saving file at: " + new File(FILENAME).getAbsolutePath());
    }

    public static Citizen save(Citizen citizen) {
        db.put(citizen.getRegistryNumber(), citizen);
        saveToFile();
        return citizen;
    }

    public static Citizen findByRegistryNumber(String registryNumber) {
        return db.get(registryNumber);
    }

    public static Map<String, Citizen> findAll() {
        return db;
    }

    public static boolean delete(String registryNumber) {
        if (db.remove(registryNumber) != null) {
            saveToFile();
            return true;
        }
        return false;
    }
}

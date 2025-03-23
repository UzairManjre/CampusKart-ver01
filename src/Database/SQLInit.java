package src.Database;

import java.io.*;
import java.util.Properties;

public class SQLInit {
    private static final String CONFIG_FILE = "db_config.properties";
    public static String URL;
    public static String USERNAME;
    public static String PASSWORD;

    static {
        loadConfig();
    }

    private static void loadConfig() {
        Properties properties = new Properties();
        File file = new File(CONFIG_FILE);

        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                properties.load(fis);
                URL = properties.getProperty("URL");
                USERNAME = properties.getProperty("USERNAME");
                PASSWORD = properties.getProperty("PASSWORD");
            } catch (IOException e) {
                System.out.println("Error reading database config file. Please enter details manually.");
                promptUserForDetails();
            }
        } else {
            System.out.println("Database config file not found. Please enter details manually.");
            promptUserForDetails();
        }
    }

    private static void promptUserForDetails() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Enter Database URL: ");
            URL = reader.readLine();
            System.out.print("Enter Database Username: ");
            USERNAME = reader.readLine();
            System.out.print("Enter Database Password: ");
            PASSWORD = reader.readLine();
            saveConfig();
        } catch (IOException e) {
            throw new RuntimeException("Error reading user input!", e);
        }
    }

    private static void saveConfig() {
        Properties properties = new Properties();
        properties.setProperty("URL", URL);
        properties.setProperty("USERNAME", USERNAME);
        properties.setProperty("PASSWORD", PASSWORD);

        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            properties.store(fos, "Database Configuration");
            System.out.println("Database configuration saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving database config file.");
        }
    }
}

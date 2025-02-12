package DAO.mongo;

import DAO.SocieteDatabaseException;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import logs.LogManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

public class ConnexionMongo {

    private static MongoClient connexion = null;
    private static MongoDatabase db = null;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (connexion != null) {
                    LogManager.logs.log(Level.INFO, "Database fermée");
                    connexion.close();
                }
            }
        });
    }

    public ConnexionMongo() {
    }

    public static MongoDatabase getInstance() throws SocieteDatabaseException {
        if (db == null) {

            final Properties dataProperties = new Properties();

            File fichier = new File("database.mongo.properties");
            FileInputStream input = null;

            try {
                input = new FileInputStream(fichier);
                dataProperties.load(input);
            } catch (FileNotFoundException e) {
                LogManager.logs.log(Level.SEVERE, e.getMessage());
                throw new SocieteDatabaseException("Fichier de paramétrage " +
                        "non trouvé.");
            } catch (IOException e) {
                LogManager.logs.log(Level.SEVERE, e.getMessage());
                throw new SocieteDatabaseException("Fichier de paramétrage " +
                        "rencontre un problème.");
            }

            // Paramètres Mongo
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(
                            dataProperties.getProperty("protocol") +
                                    dataProperties.getProperty("login") + ":" +
                                    dataProperties.getProperty("password") +
                                    dataProperties.getProperty("url")
                    ))
                    .build();

            // Creation client Mongo.
            connexion = MongoClients.create(settings);
            db = connexion.getDatabase("gestionclients");
            LogManager.logs.log(Level.INFO, "Database ouverte");
        }
        return db;
    }

    public static void close() {
        connexion.close();
    }
}

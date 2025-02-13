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

/**
 * Classe pour la connexion MongoDB.
 */
public class ConnexionMongo {

    /**
     * Objet connexion.
     */
    private static MongoClient connexion = null;

    /**
     * Objet base de données.
     */
    private static MongoDatabase db = null;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            /**
             * Méthode à lancer lors de la fermeture de l'application.
             */
            public void run() {

                if (connexion != null) {
                    // Si la connexion n'est pas déjà fermée.

                    // Log de la fermeture et fermeture de la connexion.
                    LogManager.logs.log(Level.INFO, "Database fermée");
                    connexion.close();
                }
            }
        });
    }

    /**
     * Constructor.
     */
    public ConnexionMongo() {
    }

    /**
     * Méthode qui renvoie une instance de la connexion.
     *
     * @return MongoDatabase La connexion à laquelle se raccrocher.
     * @throws SocieteDatabaseException Exception lors de la lecture du
     *                                  fichier de paramétrage.
     */
    public static MongoDatabase getInstance() throws SocieteDatabaseException {
        if (db == null) {
            // Si la base de données n'est pas déjà ouverte.

            // Initialisation des variables pour la lecture du fichier de
            // configuration.
            final Properties dataProperties = new Properties();
            File fichier = new File("database.mongo.properties");
            FileInputStream input;

            try {
                // Chargement de la configuration.
                input = new FileInputStream(fichier);
                dataProperties.load(input);
            } catch (FileNotFoundException e) {

                // Exception attrapée, logging de celle-ci et lancement d'une
                // exception compréhensible pour l'utilisateur.
                LogManager.logs.log(Level.SEVERE, e.getMessage());
                throw new SocieteDatabaseException("Fichier de paramétrage " +
                        "non trouvé.");
            } catch (IOException e) {

                // Exception attrapée, logging de celle-ci et lancement d'une
                // exception compréhensible pour l'utilisateur.
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

            // Ouverture de la connexion avec la configuration.
            connexion = MongoClients.create(settings);

            // Ouverture de la base de données.
            db = connexion.getDatabase("gestionclients");

            // Logging de l'ouverture de la connexion.
            LogManager.logs.log(Level.INFO, "Database ouverte");
        }

        // Retourne la base de données.
        return db;
    }

    /**
     * Méthode de fermeture de la connexion.
     */
    public static void close() {
        db = null;
        connexion.close();
        connexion = null;
    }
}

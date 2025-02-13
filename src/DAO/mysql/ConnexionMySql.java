package DAO.mysql;

import DAO.SocieteDatabaseException;
import logs.LogManager;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Classe pour la connexion MySql.
 */
public class ConnexionMySql {

    /**
     * Objet connexion.
     */
    private static Connection connexion = null;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            /**
             * Méthode à lancer lors de la fermeture de l'application.
             */
            public void run() {

                if (connexion != null) {
                    // Si la connexion n'est pas déjà fermée.
                    try {

                        // Log de la fermeture et fermeture de la connexion.
                        LogManager.logs.log(Level.INFO, "Database fermée");
                        connexion.close();
                    } catch (SQLException e) {
                        // Exception attrapée, log de celle-ci et envoi d'une
                        // exception à l'utilisateur.
                        LogManager.logs.log(Level.SEVERE, e.getMessage());
                        JOptionPane.showMessageDialog(null, "Problème lors de" +
                                        " la fermeture de la connexion.",
                                "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    /**
     * Constructor.
     */
    private ConnexionMySql() {

    }

    /**
     * Méthode qui renvoie une instance de la connexion.
     *
     * @return Connection La connection à laquelle se raccrocher.
     * @throws SocieteDatabaseException Exception lors de la lecture du
     *                                  fichier de paramétrage ou lors de la création de la connexion.
     */
    public static Connection getInstance() throws SocieteDatabaseException {
        if (connexion == null) {
            // Si la connexion n'est pas déjà ouverte.

            // Initialisation des variables pour la lecture du fichier de
            // configuration.
            final Properties dataProperties = new Properties();
            File fichier = new File("database.mysql.properties");
            FileInputStream input;

            try {
                // Chargement de la configuration.
                input = new FileInputStream(fichier);
                dataProperties.load(input);

                // Ouverture de la connexion avec la configuration.
                connexion = DriverManager.getConnection(
                        dataProperties.getProperty("url"),
                        dataProperties.getProperty("login"),
                        dataProperties.getProperty("password")
                );

                // Logging de l'ouverture de la connexion.
                LogManager.logs.log(Level.INFO, "Database ouverte");
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
            } catch (SQLException e) {

                // Exception attrapée, logging de celle-ci et lancement d'une
                // exception compréhensible pour l'utilisateur.
                LogManager.logs.log(Level.SEVERE, e.getMessage());
                throw new SocieteDatabaseException("La connexion n'a pu être " +
                        "ouverte");
            }
        }

        // Retourne la connexion.
        return connexion;
    }

    /**
     * Méthode de fermeture de la connexion.
     */
    public static void close() throws SocieteDatabaseException {
        try {
            connexion.close();
            connexion = null;
        } catch (SQLException e) {
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur lors de la fermeture " +
                    "de la connexion.");
        }
    }
}

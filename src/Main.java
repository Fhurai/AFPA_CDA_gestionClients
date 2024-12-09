import logs.LogManager;
import utilities.Files;
import utilities.SocieteUtilitiesException;
import view.Index;

import java.io.IOException;
import java.util.logging.Level;

/**
 * Classe de lancement de l'application.
 */
public class Main {

    /**
     * Méthode principale de lancement de l'application.
     * @param args Arguments de lancement.
     */
    public static void main(String[] args) {

        try {
            // Initialisation du gestionnaire de logs et message de lancement
            // de l'application.
            LogManager.init();
            LogManager.run();
        } catch (IOException e) {
            System.out.println("Erreur lors de l'initialisation des logs");

            System.exit(1);
        }

        try {
            // Création des fichiers de base de données si non existants.
            Files.dbCreate();
        } catch (SocieteUtilitiesException sue) {
            LogManager.logs.log(Level.SEVERE, sue.getMessage());

            System.out.println(sue.getMessage());
        } catch (Exception e) {
            LogManager.logs.log(Level.SEVERE, e.getMessage());

            System.exit(1);
        }

        try {
            // Chargement des fichiers de base de données.
            Files.dbLoad();
        } catch (SocieteUtilitiesException sue) {
            LogManager.logs.log(Level.SEVERE, sue.getMessage());

            System.out.println(sue.getMessage());
        } catch (Exception e) {
            LogManager.logs.log(Level.SEVERE, e.getMessage());

            System.exit(1);
        }

        // Affichage de la vue d'accueil.
        new Index().setVisible(true);
    }
}
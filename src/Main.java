import logs.LogManager;
import utilities.Files;
import utilities.SocieteUtilitiesException;
import view.Index;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;
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
            String message = "Erreur lors de l'initialisation des logs";
            System.out.println(message);
            System.out.println(Arrays.toString(e.getStackTrace()));
            JOptionPane.showMessageDialog(null, message);
            System.exit(1);
        } catch (Exception e) {
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            JOptionPane.showMessageDialog(null, "Erreur inconnue.");
            System.exit(1);
        }

        try {
            // Création des fichiers de base de données si non existants.
            Files.dbCreate();

            // Chargement des fichiers de base de données.
            Files.dbLoad();
        } catch (SocieteUtilitiesException sue) {
            LogManager.logs.log(Level.SEVERE, sue.getMessage());
            JOptionPane.showMessageDialog(null, sue.getMessage());
            System.out.println(sue.getMessage());
        } catch (Exception e) {
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            JOptionPane.showMessageDialog(null, "Erreur lors de la création " +
                    "ou du chargement de la base de données.");
            System.exit(1);
        }

        // Affichage de la vue d'accueil.
        new Index().setVisible(true);
    }
}
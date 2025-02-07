import DAO.mysql.ConnexionMySql;
import DAO.SocieteDatabaseException;
import logs.LogManager;
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

        try{
            // Initialisation de l'accès à la base de données.
            ConnexionMySql.getInstance();
        }catch (SocieteDatabaseException sde){
            JOptionPane.showMessageDialog(null, sde.getMessage());
            System.exit(1);
        }

        // Affichage de la vue d'accueil.
        new Index().setVisible(true);
    }
}
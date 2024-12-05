import entities.*;
import logs.LogManager;

import java.io.IOException;
import java.util.logging.Level;

public class Main {
    public static void main(String[] args) {

        try {
            LogManager.init();
        } catch (IOException e) {
            System.out.println("Erreur lors de l'initialisation des logs");
            System.exit(1);
        }

        LogManager.run();

        try {
            Clients.populateClients();
            Prospects.populateProspects();
        } catch (SocieteEntityException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            LogManager.logs.log(Level.INFO, e.getMessage());
            System.exit(1);
        }

        System.out.println(Clients.toClientsString());
        System.out.println(Prospects.toProspectsString());
    }
}
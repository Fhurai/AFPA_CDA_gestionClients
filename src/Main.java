import entities.*;

import java.time.LocalDate;

import static utilities.Formatters.FORMAT_DDMMYYYY;

public class Main {
    public static void main(String[] args) {

        try {
            Clients.populateClients();
            Prospects.populateProspects();
        } catch (SocieteEntityException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(Clients.toClientsString());
        System.out.println(Prospects.toProspectsString());
    }
}
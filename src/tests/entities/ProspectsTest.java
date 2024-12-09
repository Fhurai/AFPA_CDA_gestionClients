package entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static utilities.Formatters.FORMAT_DDMMYYYY;

/**
 * Classe de test sur la liste des prospects.
 */
class ProspectsTest {

    @BeforeEach
    void setUp() {
        try {
            Prospects.populateProspects();
        } catch (SocieteEntityException e) {
            System.out.println("Setup des tests Clients non fait !");
        }
    }

    @Test
    void toProspectsAddExistantProspect() {
        Prospect cgi = null;
        try {
            cgi = new Prospect("CGI", new Adresse("28", "Boulevard " +
                    "Albert 1er", "54000", "NANCY"), "0388553370", "contact" +
                    "@cgi.com", "", LocalDate.parse("15/11/2024", FORMAT_DDMMYYYY),
                    ReponseFermee.NON.getValue());
            assertFalse(Prospects.toProspectsAdd(cgi));
        } catch(SocieteEntityException e){
            fail(e.getMessage());
        }
    }

    @Test
    void toProspectsAddNewProspect() {
        Prospect total = null;
        LocalDate dtTotal = LocalDate.parse("27/08/2024", FORMAT_DDMMYYYY);
        try {
            total = new Prospect("Total Energies", new Adresse("2", "Place " +
                    "Jean Millier", "92400", "Courbevoie"), "0156784923",
                    "contact@totalenergies.fr", "", dtTotal,
                    ReponseFermee.NON.getValue());
            assertTrue(Prospects.toProspectsAdd(total));
        } catch(SocieteEntityException e){
            fail(e.getMessage());
        }
    }
}
package entities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test sur la liste des clients.
 */
class ClientsTest {

    @Test
    void toClientsAddExistantClient() {
        Client efluid = null;
        try {
            Clients.populateClients();
            efluid = new Client("Efluid SAS", new Adresse("2bis", "rue " +
                    "Ardant du Picq", "57004", "Metz"), "0387543400",
                    "contact@efluid.fr", "", 6388000, 250);
            assertFalse(Clients.toClientsAdd(efluid));
        } catch (SocieteEntityException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void toClientsAddNewClient() {
        Client cac = null;
        try {
            Clients.populateClients();
            cac = new Client("Centrale des Artisans Coiffeurs", new Adresse(
                    "4", "Rue du gravier", "57160", "Scy-chazelles"), "0387600600", "contact@cac.fr", "", 1448418, 100);
            assertTrue(Clients.toClientsAdd(cac));
        } catch (SocieteEntityException e) {
            fail(e.getMessage());
        }
    }
}
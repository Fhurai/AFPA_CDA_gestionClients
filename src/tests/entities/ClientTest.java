package entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test sur les instances de client.
 */
class ClientTest {

    static Client client;

    @BeforeEach
    void setUp() {
        client = new Client();
    }

    @ParameterizedTest
    @ValueSource(longs = {-10000, 100})
    void setChiffreAffairesInvalid(long chiffreAffaires) {
        assertThrows(SocieteEntityException.class, () -> client.setChiffreAffaires(chiffreAffaires));
    }

    @ParameterizedTest
    @ValueSource(longs = {200, 50000})
    void setChiffreAffairesValid(long chiffreAffaires) {
        assertDoesNotThrow(() -> client.setChiffreAffaires(chiffreAffaires));
        assertEquals(chiffreAffaires, client.getChiffreAffaires());
    }

    @ParameterizedTest
    @ValueSource(ints = {-50})
    void setNbEmployesInvalid(int nbEmployes) {
        assertThrows(SocieteEntityException.class, () -> client.setNbEmployes(nbEmployes));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 2000})
    void setNbEmployesValid(int nbEmployes) {
        assertDoesNotThrow(() -> client.setNbEmployes(nbEmployes));
        assertEquals(nbEmployes, client.getNbEmployes());

    }
}
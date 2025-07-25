package tests.entities;

import entities.Adresse;
import entities.SocieteEntityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test sur les instances d'adresse.
 */
class AdresseTest {

    static Adresse adresse;

    @BeforeEach
    void setUp() {
        adresse = new Adresse();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"aled", "br45", "2bis", ""})
    void setNumeroRueInvalid(String numeroRue) {
        assertThrows(SocieteEntityException.class, () -> adresse.setNumeroRue(numeroRue));
    }

    @ParameterizedTest
    @ValueSource(strings = {"145", "67b", "2 bis", "3", "1 ter"})
    void setNumeroRueValid(String numeroRue) {
        assertDoesNotThrow(() -> adresse.setNumeroRue(numeroRue));
        assertEquals(numeroRue, adresse.getNumeroRue());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"_-", "2 rue de la Boustifaille"})
    void setNomRueInvalid(String nomRue) {
        assertThrows(SocieteEntityException.class, () -> adresse.setNomRue(nomRue));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Rue de la Cheneau", "Avenue des Champs Elysées",
            "Rue Litaldus", "Rue Serpenoise"})
    void setNomRueValid(String nomRue) {
        assertDoesNotThrow(() -> adresse.setNomRue(nomRue));
        assertEquals(nomRue, adresse.getNomRue());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"4545", "123456", "-_'", "aled"})
    void setCodePostalInvalid(String codePostal) {
        assertThrows(SocieteEntityException.class, () -> adresse.setCodePostal(codePostal));
    }

    @ParameterizedTest
    @ValueSource(strings = {"75001", "57070", "57130", "57004"})
    void setCodePostalValid(String codePostal) {
        assertDoesNotThrow(() -> adresse.setCodePostal(codePostal));
        assertEquals(codePostal, adresse.getCodePostal());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"21JumpStreet", "45000", "1 rue Pré Longeau"})
    void setVilleInvalid(String ville) {
        assertThrows(SocieteEntityException.class, () -> adresse.setVille(ville));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Metz", "Nancy", "Paris", "Toulouse", "Châlons-en-Champagne"})
    void setVilleValid(String ville) {
        assertDoesNotThrow(() -> adresse.setVille(ville));
        assertEquals(ville, adresse.getVille());
    }
}
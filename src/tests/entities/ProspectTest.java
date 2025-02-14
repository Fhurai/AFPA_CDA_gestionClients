package tests.entities;

import entities.Prospect;
import entities.SocieteEntityException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test sur les instances de prospect.
 */
class ProspectTest {

    static Prospect prospect;

    @BeforeEach
    void setUp() {
        prospect = new Prospect();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"21 Décembre 1992"})
    void setDateProspectionInvalid(String date) {
        assertThrows(SocieteEntityException.class, () -> {
            prospect.setDateProspection(date);
        });
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"Nope", "Yes", "yipee"})
    void setProspectInteresseInvalid(String prospectInteresse) {
        assertThrows(SocieteEntityException.class, () -> {
            prospect.setProspectInteresse(prospectInteresse);
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"Oui", "non"})
    void setProspectInteresseValid(@NotNull String prospectInteresse) {
        assertDoesNotThrow(() -> {
            prospect.setProspectInteresse(prospectInteresse);
        });
        assertEquals(prospectInteresse.toUpperCase(),
                prospect.getProspectInteresse().toUpperCase());
    }
}
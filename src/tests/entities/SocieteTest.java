package entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class SocieteTest {

    static Societe societe;

    @BeforeEach
    void setUp() {
        societe = new Societe();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"Test", "Texte relativement long qui permet " +
            "de voir la gestion de la longueur."})
    void setCommentaires(String commentaire) {
        assertDoesNotThrow(() -> societe.setCommentaires(commentaire));
    }

    @Test
    void setMail() {
    }

    @Test
    void setTelephone() {
    }

    @Test
    void setAdresse() {
    }

    @Test
    void setRaisonSociale() {
    }

    @Test
    void setIdentifiant() {
    }
}
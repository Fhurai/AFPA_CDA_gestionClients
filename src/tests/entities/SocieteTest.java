package entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class SocieteTest {

    static Societe societe;

    @BeforeEach
    void setUp() {
        societe = new Societe();
        // TODO : Remplir la liste des clients et des prospects avec des
        //  sociétés.
    }

    @ParameterizedTest
    @NullSource
    void setCommentairesInvalid(String commentaire) {
        assertThrows(SocieteEntityException.class,
                () -> {
                    societe.setCommentaires(commentaire);
                });
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"Test", "Texte relativement long qui permet " +
            "de voir la gestion de la longueur."})
    void setCommentairesValid(String commentaire) {
        assertDoesNotThrow(() -> societe.setCommentaires(commentaire));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"@free.fr", "lucas@", "lucas.kuntz.afpa", "honey " +
            "bee lovely", "test"})
    void setMailInvalid(String mail) {
        assertThrows(SocieteEntityException.class, () -> societe.setMail(mail));
    }

    @ParameterizedTest
    @ValueSource(strings = {"kuntz.lucas@gmail.com", "kulu57@live.com", "luku" +
            "@free.fr", "aaa@bbb.ccc"})
    void setMailValid(String mail) {
        assertDoesNotThrow(() -> societe.setMail(mail));
        assertEquals(mail, societe.getMail());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"1234567890", "019487236", "telephone0", "luku" +
            "@free.fr", "+33778810469", "192125746360567"})
    void setTelephoneInvalid(String telephone) {
        assertThrows(SocieteEntityException.class, () -> societe.setTelephone(telephone));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0778810469", "0354626298", "0147896325"})
    void setTelephoneValid(String telephone) {
        assertDoesNotThrow(() -> societe.setTelephone(telephone));
        assertEquals(telephone, societe.getTelephone());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void setRaisonSocialeInvalid(String raisonSociale) {
        assertThrows(SocieteEntityException.class, () -> societe.setRaisonSociale(raisonSociale));
        // TODO : Vérifier raison sociale déjà existante
    }

    @ParameterizedTest
    @ValueSource(strings = {"Google", "Apple", "facebook", "Amaz0n",
            "Micr0s0ft"})
    void setRaisonSocialeValid(String raisonSociale) {
        assertDoesNotThrow(() -> societe.setRaisonSociale(raisonSociale));
        assertEquals(raisonSociale, societe.getRaisonSociale());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -5})
    void setIdentifiantInvalid(int identifiant) {
        assertThrows(SocieteEntityException.class, () -> societe.setIdentifiant(identifiant));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 500, 123456789})
    void setIdentifiant(int identifiant) {
        assertDoesNotThrow(() -> societe.setIdentifiant(identifiant));
        assertEquals(identifiant, societe.getIdentifiant());
        // TODO : Vérifier si identifiant pas déjà donné
    }
}
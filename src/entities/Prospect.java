package entities;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static utilities.Formatters.FORMAT_DDMMYYYY;

/**
 * Classe Prospect
 */
public final class Prospect extends Societe {

    // Variables d'instance
    private LocalDate dateProspection;
    private String prospectInteresse;

    /**
     * Constructeur avec toutes les variables
     *
     * @param identifiant       Identifiant
     * @param raisonSociale     La raison sociale, ou nom
     * @param adresse           Adresse
     * @param telephone         Le numéro de téléphone
     * @param mail              L'adresse mail
     * @param commentaires      Les commentaires
     * @param dateProspection   La date de prospection
     * @param prospectInteresse Indication si le prospect est intéressé ou non
     */
    public Prospect(int identifiant, String raisonSociale, Adresse adresse,
                    String telephone, String mail, String commentaires, LocalDate dateProspection,
                    String prospectInteresse) throws SocieteEntityException {
        super(identifiant, raisonSociale, adresse, telephone, mail, commentaires);
        this.dateProspection = dateProspection;
        this.prospectInteresse = prospectInteresse;
    }

    /**
     * Constructeur avec toutes les variables
     *
     * @param raisonSociale     La raison sociale, ou nom
     * @param adresse           Adresse
     * @param telephone         Le numéro de téléphone
     * @param mail              L'adresse mail
     * @param commentaires      Les commentaires
     * @param dateProspection   La date de prospection
     * @param prospectInteresse Indication si le prospect est intéressé ou non
     */
    public Prospect(String raisonSociale, Adresse adresse, String telephone,
                    String mail, String commentaires, LocalDate dateProspection,
                    String prospectInteresse) throws SocieteEntityException {
        super(raisonSociale, adresse, telephone, mail, commentaires);
        setDateProspection(dateProspection);
        setProspectInteresse(prospectInteresse);
    }

    /**
     * Constructeur implicite
     */
    public Prospect() {
        super();
    }

    /**
     * Getter date de prospection
     *
     * @return Date de prospection
     */
    public LocalDate getDateProspection() {
        return dateProspection;
    }

    /**
     * Setter date de prospection
     *
     * @param dateProspection La date de prospection
     */
    public void setDateProspection(LocalDate dateProspection) {
        this.dateProspection = dateProspection;
    }

    /**
     * Setter date de prospection
     *
     * @param dateProspection La date de prospection sous forme de chaîne de
     *                        caractères
     */
    public void setDateProspection(String dateProspection) throws SocieteEntityException {

        try {
            // Tente de changer la chaîne de caractères en date avant de
            // valoriser la propriété du prospect.
            this.dateProspection = LocalDate.parse(dateProspection, FORMAT_DDMMYYYY);
        } catch (DateTimeParseException e) {
            // Erreur lors du changement, lancement exception
            throw new SocieteEntityException("La date de prospection doit " +
                    "être au format jj/mm/aaaa !");
        } catch (NullPointerException e) {
            // Erreur lors du changement, lancement exception
            throw new SocieteEntityException("La date de prospection ne peut " +
                    "être null");
        }
    }

    /**
     * Getter prospect intéressé
     *
     * @return Indication si prospect est intéressé
     */
    public String getProspectInteresse() {
        return prospectInteresse;
    }

    /**
     * Setter prospect intéressé
     *
     * @param prospectInteresse Indication si prospect est intéressé
     */
    public void setProspectInteresse(String prospectInteresse) throws SocieteEntityException {
        if (!ReponseFermee.exists(prospectInteresse)) {
            throw new SocieteEntityException("La réponse du prospect doit " +
                    "être 'oui' ou 'non' !");
        }
        this.prospectInteresse = prospectInteresse;
    }

    /**
     * Méthode pour convertir le prospect en chaîne de caractères.
     *
     * @return le prospect en chaîne de caractères.
     */
    @Override
    public String toString() {
        return super.toString() +
                " dateProspection=" + getDateProspection() +
                ", prospectInteresse='" + getProspectInteresse() + '\'';
    }
}

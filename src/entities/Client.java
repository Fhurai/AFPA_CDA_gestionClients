package entities;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Classe Client
 */
public final class Client extends Societe {

    // Variables d'instance
    private long chiffreAffaires;
    private int nbEmployes;
    private ArrayList<Contrat> contrats;

    /**
     * Constructeur avec toutes les variables
     * @param identifiant Identifiant
     * @param raisonSociale La raison sociale, ou nom
     * @param adresse Adresse
     * @param telephone Le numéro de téléphone
     * @param mail L'adresse mail
     * @param commentaires Les commentaires
     * @param chiffreAffaires Le chiffre d'affaires
     * @param nbEmployes Le nombre d'employés
     */
    public Client(int identifiant, String raisonSociale, Adresse adresse, String telephone, String mail, String commentaires, long chiffreAffaires, int nbEmployes) throws SocieteEntityException {
        super(identifiant, raisonSociale, adresse, telephone, mail, commentaires);
        setChiffreAffaires(chiffreAffaires);
        setNbEmployes(nbEmployes);
        setContrats(new ArrayList<>());
    }

    /**
     * Constructeur avec toutes les variables
     * @param raisonSociale La raison sociale, ou nom
     * @param adresse Adresse
     * @param telephone Le numéro de téléphone
     * @param mail L'adresse mail
     * @param commentaires Les commentaires
     * @param chiffreAffaires Le chiffre d'affaires
     * @param nbEmployes Le nombre d'employés
     */
    public Client(String raisonSociale, Adresse adresse, String telephone, String mail, String commentaires, long chiffreAffaires, int nbEmployes) throws SocieteEntityException {
        super(raisonSociale, adresse, telephone, mail, commentaires);
        setChiffreAffaires(chiffreAffaires);
        setNbEmployes(nbEmployes);
        setContrats(new ArrayList<>());
    }

    /**
     * Constructeur implicite
     */
    public Client() {
        super();
        setContrats(new ArrayList<>());
    }

    /**
     * Getter chiffre d'affaires
     * @return Chiffre d'affaires
     */
    public long getChiffreAffaires() {
        return chiffreAffaires;
    }

    /**
     * Setter chiffres d'affaires
     * @param chiffreAffaires Chiffre d'affaires
     */
    public void setChiffreAffaires(long chiffreAffaires) throws SocieteEntityException {
        // Cas nombre négatif
        if(chiffreAffaires < 200){
            throw new SocieteEntityException("Le chiffre d'affaire doit être " +
                    "supérieur à 200");
        }

        this.chiffreAffaires = chiffreAffaires;
    }

    /**
     * Getter nombre d'employés
     * @return Nombre d'employés
     */
    public int getNbEmployes() {
        return nbEmployes;
    }

    /**
     * Setter nombre d'employés
     * @param nbEmployes Nombre d'employés
     */
    public void setNbEmployes(int nbEmployes) throws SocieteEntityException {
        // Cas nombre négatif
        if(nbEmployes < 0){
            throw new SocieteEntityException("Le nombre d'employés ne peut " +
                    "être négatif");
        }

        this.nbEmployes = nbEmployes;
    }

    /**
     * Getter contrats.
     *
     * @return Liste des contrats du client.
     */
    public ArrayList<Contrat> getContrats() {
        return contrats;
    }

    /**
     * Setter contrats.
     *
     * @param contrats La nouvelle liste des contrats.
     */
    public void setContrats(ArrayList<Contrat> contrats) {
        this.contrats = contrats;
    }

    /**
     * Méthode pour convertir le client en chaîne de caractères.
     * @return le client en chaîne de caractères.
     */
    @Override
    public @NotNull String toString() {
        return super.toString() +
                " chiffreAffaires=" + getChiffreAffaires() +
                ", nbEmployes=" + getNbEmployes();
    }
}

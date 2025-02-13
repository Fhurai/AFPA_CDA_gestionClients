package entities;

import utilities.Patterns;

/**
 * Classe Adresse
 */
public class Adresse {

    // Variables d'instance.
    private int identifiant;
    private String numeroRue;
    private String nomRue;
    private String codePostal;
    private String ville;

    /**
     * Constructeur avec toutes les variables
     *
     * @param identifiant Identifiant de l'adresse
     * @param numeroRue   Numéro de la rue
     * @param nomRue      Nom de la rue
     * @param codePostal  Code postal
     * @param ville       La ville
     * @throws SocieteEntityException Lance une exception si une des variables
     *                                présente un problème
     */
    public Adresse(int identifiant, String numeroRue, String nomRue,
                   String codePostal, String ville)
            throws SocieteEntityException {
        setIdentifiant(identifiant);
        setNumeroRue(numeroRue);
        setNomRue(nomRue);
        setCodePostal(codePostal);
        setVille(ville);
    }

    /**
     * Constructeur implicite
     */
    public Adresse() {
    }

    /**
     * Getter identifiant.
     *
     * @return Identifiant de l'adresse.
     */
    public int getIdentifiant() {
        return identifiant;
    }

    /**
     * Setter identifiant.
     *
     * @param identifiant Le nouvel identifiant.
     */
    public void setIdentifiant(int identifiant) throws SocieteEntityException {
        if (identifiant < 0) {
            throw new SocieteEntityException("L'identifiant ne peut pas être " +
                    "inférieur à 0.");
        }
        this.identifiant = identifiant;
    }

    /**
     * Getter numéro rue.
     *
     * @return Numéro de la rue.
     */
    public String getNumeroRue() {
        return numeroRue;
    }

    /**
     * Setter numéro rue.
     *
     * @param numeroRue Numéro de la rue.
     * @throws SocieteEntityException Lance une exception si le numéro de rue n'est
     *                                pas valide.
     */
    public void setNumeroRue(String numeroRue) throws SocieteEntityException {
        // Cas String vide ou null
        if (numeroRue == null || numeroRue.isEmpty()) {
            throw new SocieteEntityException("Le numéro de rue de l'adresse ne peut" +
                    " être vide !");
        }

        // Cas regex non matché
        if (!Patterns.PATTERN_NUMERO_RUE.matcher(numeroRue).matches()) {
            throw new SocieteEntityException("Le numéro de rue doit être un nombre," +
                    " éventuellement suivi d'une ou plusieurs lettres.");
        }

        this.numeroRue = numeroRue;
    }

    /**
     * Getter nom rue
     *
     * @return Nom de la rue.
     */
    public String getNomRue() {
        return nomRue;
    }

    /**
     * Setter nom rue
     *
     * @param nomRue Nom de la rue
     * @throws SocieteEntityException Lance une exception si le nom de la rue n'est
     *                                pas valide.
     */
    public void setNomRue(String nomRue) throws SocieteEntityException {
        // Cas chaîne vide ou null
        if (nomRue == null || nomRue.isEmpty()) {
            throw new SocieteEntityException("Le nom de rue de l'adresse ne peut" +
                    " être vide !");
        }

        // Cas regex non matché
        if (!Patterns.PATTERN_NOM_RUE.matcher(nomRue).matches()) {
            throw new SocieteEntityException("Le nom de rue ne doit pas comporter " +
                    "de nombre");
        }

        this.nomRue = nomRue;
    }

    /**
     * Getter code postal
     *
     * @return Code postal
     */
    public String getCodePostal() {
        return codePostal;
    }

    /**
     * Setter code postal
     *
     * @param codePostal Code postal
     * @throws SocieteEntityException Lance une exception si le code postal n'est
     *                                pas valide.
     */
    public void setCodePostal(String codePostal) throws SocieteEntityException {
        // Cas chaîne vide ou null.
        if (codePostal == null || codePostal.isEmpty()) {
            throw new SocieteEntityException("Le code postal de l'adresse ne peut" +
                    " être vide !");
        }

        // Cas regex non matché
        if (!Patterns.PATTERN_CODE_POSTAL.matcher(codePostal).matches()) {
            throw new SocieteEntityException("Le code postal de l'adresse est un " +
                    "groupe de cinq chiffres !");
        }

        this.codePostal = codePostal;
    }

    /**
     * Getter ville
     *
     * @return Ville
     */
    public String getVille() {
        return ville;
    }

    /**
     * Setter ville
     *
     * @param ville Ville
     * @throws SocieteEntityException Lance une exception si la ville n'est pas
     *                                valide.
     */
    public void setVille(String ville) throws SocieteEntityException {
        // Cas chaîne vide ou null
        if (ville == null || ville.isEmpty()) {
            throw new SocieteEntityException("La ville de l'adresse ne peut être " +
                    "vide !");
        }

        // Cas regex non matché
        if (!Patterns.PATTERN_VILLE.matcher(ville).matches()) {
            throw new SocieteEntityException("La ville est composée de " +
                    "lettres uniquement !");
        }

        this.ville = ville;
    }

    /**
     * Méthode pour convertir l'adresse en chaîne de caractères.
     *
     * @return l'adresse en chaîne de caractères.
     */
    @Override
    public String toString() {
        return getNumeroRue() + " " + getNomRue() + ", " + getCodePostal() +
                " " + getVille();
    }
}

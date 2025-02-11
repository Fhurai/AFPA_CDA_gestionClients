package entities;

/**
 * Classe Contrat
 */
public class Contrat {

    // Variables d'instance
    private int identifiant;
    private int idClient;
    private String libelle;
    private double montant;

    /**
     * Constructeur avec toutes les variables.
     *
     * @param identifiant Identifiant du contrat.
     * @param idClient Identifiant du client derrière le contrat.
     * @param libelle Libellé du contrat.
     * @param montant Montant de contrat.
     */
    public Contrat(int identifiant, int idClient, String libelle, double montant) throws SocieteEntityException {
        this.setIdentifiant(identifiant);
        this.setIdClient(idClient);
        this.setLibelle(libelle);
        this.setMontant(montant);
    }

    /**
     * Constructeur sans identifiant.
     *
     * @param idClient Identifiant du client derrière le contrat.
     * @param libelle Libellé du contrat.
     * @param montant Montant de contrat.
     */
    public Contrat(int idClient, String libelle, double montant) throws SocieteEntityException {
        this.setIdClient(idClient);
        this.setLibelle(libelle);
        this.setMontant(montant);
    }

    /**
     * Constructeur implicite.
     */
    public Contrat() {
    }

    /**
     * Getter identifiant contrat.
     *
     * @return Identifiant du contrat.
     */
    public int getIdentifiant() {
        return identifiant;
    }

    /**
     * Setter identifiant contrat.
     *
     * @param identifiant Le nouvel identifiant.
     */
    public void setIdentifiant(int identifiant) throws SocieteEntityException {
        // Cas nombre négatif
        if(identifiant <= 0){
            throw new SocieteEntityException("L'identifiant ne peut être " +
                    "inférieur ou égal à 0 !");
        }
        this.identifiant = identifiant;
    }

    /**
     * Getter identifiant client.
     *
     * @return Identifiant client.
     */
    public int getIdClient() {
        return idClient;
    }

    /**
     * Setter identifiant client.
     *
     * @param idClient Le nouvel identifiant de client.
     */
    public void setIdClient(int idClient) throws SocieteEntityException {
        // Cas nombre négatif
        if(identifiant <= 0){
            throw new SocieteEntityException("L'identifiant du client ne peut" +
                    " être inférieur ou égal à 0 !");
        }
        this.idClient = idClient;
    }

    /**
     * Getter libellé.
     *
     * @return Libellé du contrat.
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Setter libellé.
     *
     * @param libelle Le nouveau libellé du contrat.
     */
    public void setLibelle(String libelle) throws SocieteEntityException {
        if(libelle == null || libelle.isEmpty()){
            throw new SocieteEntityException("Le libellé du contrat ne peut " +
                    "pas être vide.");
        }
        this.libelle = libelle;
    }

    /**
     * Getter montant.
     *
     * @return montant du contrat.
     */
    public double getMontant() {
        return montant;
    }

    /**
     * Setter montant.
     *
     * @param montant Le nouveau montant.
     */
    public void setMontant(double montant) throws SocieteEntityException {
        if(montant < 0){
            throw new SocieteEntityException("Un contrat ne peut pas avoir de" +
                    " montant négatif.");
        }
        this.montant = montant;
    }

    @Override
    public String toString() {
        return "Contrat n°"  + this.getIdentifiant() +
                " pour le client n°" + this.getIdClient() +
                " nommé '" + this.getLibelle() + '\'' +
                ", de valeur " + this.getMontant() + "€";
    }
}

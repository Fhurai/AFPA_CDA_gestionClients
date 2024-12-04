package entities;

/**
 * Classe Société
 */
public class Societe {

    // Variables d'instance
    private int identifiant;
    private String raisonSociale;
    private Adresse adresse;
    private String telephone;
    private String mail;
    private String commentaires;

    /**
     * Constructeur avec toutes les variables
     * @param raisonSociale La raison sociale, ou nom
     * @param adresse Adresse
     * @param telephone Le numéro de téléphone
     * @param mail L'adresse mail
     * @param commentaires Les commentaires
     */
    public Societe(String raisonSociale, Adresse adresse, String telephone, String mail, String commentaires) {
        setIdentifiant(0);
        setRaisonSociale(raisonSociale);
        setAdresse(adresse);
        setTelephone(telephone);
        setMail(mail);
        setCommentaires(commentaires);
    }

    /**
     * Constructeur implicite
     */
    public Societe() {
    }

    /**
     * Getter commentaires
     * @return Commentaires
     */
    public String getCommentaires() {
        return commentaires;
    }

    /**
     * Setter commentaires
     * @param commentaires Commentaires, possiblement null
     */
    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    /**
     * Getter mail
     * @return Adresse mail
     */
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * Getter téléphone
     * @return Numéro de téléphone
     */
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * Getter adresse
     * @return Adresse
     */
    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    /**
     * Getter raison social
     * @return Raison sociale, ou nom
     */
    public String getRaisonSociale() {
        return raisonSociale;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    /**
     * Getter identifiant
     * @return Identifiant
     */
    public int getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(int identifiant) {
        this.identifiant = identifiant;
    }

    /**
     * Méthode pour convertir la société en chaîne de caractères.
     * @return la société en chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Societe{" +
                "identifiant=" + identifiant +
                ", raisonSociale='" + raisonSociale + '\'' +
                ", adresse=" + adresse +
                ", telephone='" + telephone + '\'' +
                ", mail='" + mail + '\'' +
                ", commentaires='" + commentaires + '\'' +
                '}';
    }

    /**
     * Indique si la raison sociale est unique ou non
     * @return Indication si la raison sociale est unique.
     */
    private boolean isRaisonSocialUnique(){
        return false;
    }
}

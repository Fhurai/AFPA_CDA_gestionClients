package entities;

import DAO.SocieteDatabaseException;
import DAO.mysql.MySqlFactory;
import logs.LogManager;
import utilities.Patterns;

import java.util.logging.Level;

/**
 * Classe Société
 */
abstract public class Societe {

    // Variables d'instance
    private int identifiant;
    private String raisonSociale;
    private Adresse adresse;
    private String telephone;
    private String mail;
    private String commentaires;

    /**
     * Constructeur avec toutes les variables
     * @param identifiant Identifiant
     * @param raisonSociale La raison sociale, ou nom
     * @param adresse Adresse
     * @param telephone Le numéro de téléphone
     * @param mail L'adresse mail
     * @param commentaires Les commentaires
     */
    public Societe(int identifiant, String raisonSociale, Adresse adresse, String telephone, String mail, String commentaires) throws SocieteEntityException {
        setIdentifiant(identifiant);
        setRaisonSociale(raisonSociale);
        setAdresse(adresse);
        setTelephone(telephone);
        setMail(mail);
        setCommentaires(commentaires);
    }

    /**
     * Constructeur avec toutes les variables sauf l'identifiant
     * @param raisonSociale La raison sociale, ou nom
     * @param adresse Adresse
     * @param telephone Le numéro de téléphone
     * @param mail L'adresse mail
     * @param commentaires Les commentaires
     */
    public Societe(String raisonSociale, Adresse adresse, String telephone, String mail, String commentaires) throws SocieteEntityException {
        this.identifiant = 0;
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

    /**
     * Setter adresse mail
     * @param mail L'adresse mail
     */
    public void setMail(String mail) throws SocieteEntityException {
        // Cas chaîne vide ou null
        if(mail == null || mail.isEmpty()){
            throw new SocieteEntityException("L'adresse mail ne peut être " +
                    "vide !");
        }

        // Cas regex non matché
        if(!Patterns.PATTERN_MAIL.matcher(mail).matches()){
            throw new SocieteEntityException("L'adresse mail doit être de " +
                    "format destinataire@fournisseur.extension");
        }
        this.mail = mail;
    }

    /**
     * Getter téléphone
     * @return Numéro de téléphone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Setter téléphone
     * @param telephone Le numéro de téléphone
     */
    public void setTelephone(String telephone) throws SocieteEntityException {
        // Cas chaîne vide ou null
        if(telephone == null || telephone.isEmpty()){
            throw new SocieteEntityException("Le numéro de téléphone ne peut " +
                    "être vide !");
        }

        // Cas regex non matché
        if(!Patterns.PATTERN_TELEPHONE.matcher(telephone).matches()){
            throw new SocieteEntityException("Le numéro de téléphone doit " +
                    "comporter maximum quinze chiffres !");
        }

        this.telephone = telephone;
    }

    /**
     * Getter adresse
     * @return Adresse
     */
    public Adresse getAdresse() {
        return adresse;
    }

    /**
     * Setter adresse
     * @param adresse L'adresse
     */
    public void setAdresse(Adresse adresse) throws SocieteEntityException {
        // Cas chaîne vide
        if(adresse == null){
            throw new SocieteEntityException("L'adresse ne peut être vide !");
        }

        this.adresse = adresse;
    }

    /**
     * Getter raison social
     * @return Raison sociale, ou nom
     */
    public String getRaisonSociale() {
        return raisonSociale;
    }

    /**
     * Setter raison sociale
     * @param raisonSociale La raison sociale, ou nom
     */
    public void setRaisonSociale(String raisonSociale) throws SocieteEntityException {
        // Cas chaîne vide ou null
        if(raisonSociale == null || raisonSociale.isEmpty()){
            throw new SocieteEntityException("La raison sociale ne peut être" +
                    " vide !");
        }

//        try {
//            for(Client c : MySqlFactory.getClientsDAO().findAll()){
//                if(raisonSociale.equals(c.getRaisonSociale()) && this.identifiant != c.getIdentifiant()){
//                    throw new SocieteEntityException("La raison sociale donnée " +
//                            "existe déjà en base de données.");
//                }
//            }
//
//            for(Prospect p : MySqlFactory.getProspectsDAO().findAll()){
//                if(raisonSociale.equals(p.getRaisonSociale()) && this.identifiant != p.getIdentifiant()){
//                    throw new SocieteEntityException("La raison sociale donnée " +
//                            "existe déjà en base de données.");
//                }
//            }
//        } catch (SocieteDatabaseException e) {
//            LogManager.logs.log(Level.SEVERE, e.getMessage());
//            throw new SocieteEntityException("La vérification de l'unicité de" +
//                    " la raison sociale ne peut être faite !");
//        }
        this.raisonSociale = raisonSociale;
    }

    /**
     * Getter identifiant
     * @return Identifiant
     */
    public int getIdentifiant() {
        return identifiant;
    }

    /**
     * Setter identifiant
     * @param identifiant L'identifiant
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
     * Méthode pour convertir la société en chaîne de caractères.
     * @return la société en chaîne de caractères.
     */
    @Override
    public String toString() {
        return "identifiant=" + getIdentifiant() +
                ", raisonSociale='" + getRaisonSociale() + '\'' +
                ", adresse=" + getAdresse() +
                ", telephone='" + getTelephone() + '\'' +
                ", mail='" + getMail() + '\'' +
                ", commentaires='" + getCommentaires() + '\'';
    }
}

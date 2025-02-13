package builders;

import entities.Adresse;
import entities.Client;
import entities.Contrat;
import entities.SocieteEntityException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Classe constructrice Client.
 */
public class ClientBuilder extends SocieteBuilder<Client> {

    /**
     * Constructor.
     */
    public ClientBuilder() {
        super(new Client());
    }

    /**
     * New builder from static call.
     *
     * @return new ClientBuilder
     */
    @Contract(" -> new")
    public static @NotNull ClientBuilder getNewClientBuilder() {
        return new ClientBuilder();
    }

    /**
     * Setter identifiant.
     *
     * @param identifiant Nouvel identifiant.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the identifiant setter.
     */
    public ClientBuilder dIdentifiant(int identifiant) throws SocieteEntityException {
        this.getEntity().setIdentifiant(identifiant);
        return this;
    }

    /**
     * Setter identifiant.
     *
     * @param identifiant Nouvel identifiant.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the identifiant setter.
     */
    public ClientBuilder dIdentifiant(String identifiant) throws SocieteEntityException {
        return dIdentifiant(Integer.parseInt(identifiant));
    }

    /**
     * Setter Raison Sociale
     *
     * @param raisonSociale Nouvelle raison sociale.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the raisonSociale setter.
     */
    public ClientBuilder deRaisonSociale(String raisonSociale) throws SocieteEntityException {
        this.getEntity().setRaisonSociale(raisonSociale);
        return this;
    }

    /**
     * Setter Adresse.
     *
     * @param adresse Nouvelle adresse.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the adresse setter.
     */
    public ClientBuilder dAdresse(Adresse adresse) throws SocieteEntityException {
        this.getEntity().setAdresse(adresse);
        return this;
    }

    /**
     * Setter Adresse.
     *
     * @param identifiant Nouvel identifiant.
     * @param numRue Nouveau numéro de rue.
     * @param nomRue Nouveau nom de rue.
     * @param codePostal Nouveau code postal.
     * @param ville Nouvelle ville.
     * @return This builder.
     * @throws SocieteEntityException Exception set by one of Adresse setter.
     */
    public ClientBuilder avecAdresse(String identifiant, String numRue,
                                     String nomRue,
                                     String codePostal, String ville) throws SocieteEntityException {
        this.getEntity().setAdresse(AdresseBuilder.getNewAdresseBuilder()
                .dIdentifiant(identifiant)
                .deNumeroRue(numRue)
                .deNomRue(nomRue)
                .deCodePostal(codePostal)
                .deVille(ville)
                .build());
        return this;
    }

    /**
     * Setter Telephone.
     *
     * @param telephone Nouveau numéro de téléphone.
     * @return This builder.
     * @throws SocieteEntityException Exception set by telephone setter.
     */
    public ClientBuilder deTelephone(String telephone) throws SocieteEntityException {
        this.getEntity().setTelephone(telephone);
        return this;
    }

    /**
     * Setter Mail.
     *
     * @param mail Nouveau mail.
     * @return This builder.
     * @throws SocieteEntityException Exception set by mail setter.
     */
    public ClientBuilder deMail(String mail) throws SocieteEntityException {
        this.getEntity().setMail(mail);
        return this;
    }

    /**
     * Setter Commentaires.
     *
     * @param commentaires Nouveaux commentaires.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the commentaires setter.
     */
    public ClientBuilder deCommentaires(String commentaires) throws SocieteEntityException {
        this.getEntity().setCommentaires(commentaires);
        return this;
    }

    /**
     * Setter Chiffres d'Affaires
     *
     * @param chiffreAffaires Nouveau chiffre d'affaires.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the chiffreAffaires
     *                                setter.
     */
    public ClientBuilder deChiffreAffaires(long chiffreAffaires) throws SocieteEntityException {
        this.getEntity().setChiffreAffaires(chiffreAffaires);
        return this;
    }

    /**
     * Setter Chiffres d'Affaires
     *
     * @param chiffreAffaires Nouveau chiffre d'affaires.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the chiffreAffaires
     *                                setter.
     */
    public ClientBuilder deChiffreAffaires(String chiffreAffaires) throws SocieteEntityException {
        double ca = Double.parseDouble(chiffreAffaires);
        return this.deChiffreAffaires((long) ca);
    }

    /**
     * Setter Nombre Employés.
     *
     * @param nombreEmployes Nouveau nombre d'employés
     * @return This builder.
     * @throws SocieteEntityException Exception set by the NbEmployes setter.
     */
    public ClientBuilder deNombreEmployes(int nombreEmployes) throws SocieteEntityException {
        this.getEntity().setNbEmployes(nombreEmployes);
        return this;
    }

    /**
     * Setter Nombre Employés.
     *
     * @param nombreEmployes Nouveau nombre d'employés
     * @return This builder.
     * @throws SocieteEntityException Exception set by the NbEmployes setter.
     */
    public ClientBuilder deNombreEmployes(String nombreEmployes) throws SocieteEntityException {
        this.getEntity().setNbEmployes(Integer.parseInt(nombreEmployes));
        return this;
    }

    /**
     * Setter Contrats.
     *
     * @param contrats Nouvelle liste des contrats.
     * @return This builder.
     */
    public ClientBuilder deContrats(ArrayList<Contrat> contrats) {
        this.getEntity().setContrats(contrats);
        return this;
    }

    /**
     * Ajout un contrat au client.
     *
     * @param contrat Nouveau contrat à ajouter.
     * @return This builder.
     */
    public ClientBuilder ajouterContrat(Contrat contrat) {
        this.getEntity().getContrats().add(contrat);
        return this;
    }

    /**
     * Getter Client construit.
     *
     * @return Getter Client construit.
     */
    public Client build() {
        return this.getEntity();
    }
}

package builders;

import entities.Adresse;
import entities.Prospect;
import entities.SocieteEntityException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import utilities.Formatters;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ProspectBuilder extends SocieteBuilder<Prospect> {

    /**
     * Constructor.
     */
    public ProspectBuilder() {
        super(new Prospect());
    }

    /**
     * New builder from static call.
     *
     * @return new ProspectBuilder.
     */
    @Contract(" -> new")
    public static @NotNull ProspectBuilder getNewProspectBuilder(){
        return new ProspectBuilder();
    }

    /**
     * Setter identifiant.
     *
     * @param identifiant Nouvel identifiant.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the identifiant setter.
     */
    public ProspectBuilder dIdentifiant(int identifiant) throws SocieteEntityException {
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
    public ProspectBuilder dIdentifiant(String identifiant) throws SocieteEntityException {
        return dIdentifiant(Integer.parseInt(identifiant));
    }

    /**
     * Setter Raison Sociale
     *
     * @param raisonSociale Nouvelle raison sociale.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the raisonSociale setter.
     */
    public ProspectBuilder deRaisonSociale(String raisonSociale) throws SocieteEntityException {
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
    public ProspectBuilder dAdresse(Adresse adresse) throws SocieteEntityException {
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
    public ProspectBuilder avecAdresse(String identifiant, String numRue,
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
    public ProspectBuilder deTelephone(String telephone) throws SocieteEntityException {
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
    public ProspectBuilder deMail(String mail) throws SocieteEntityException {
        this.getEntity().setMail(mail);
        return this;
    }

    /**
     * Setter Commentaires.
     *
     * @param commentaires Nouveaux commentaires.
     * @return This builder.
     */
    public ProspectBuilder deCommentaires(String commentaires) {
        this.getEntity().setCommentaires(commentaires);
        return this;
    }

    /**
     * Setter Date Prospection.
     *
     * @param date Nouvelle date.
     * @return This builder.
     */
    public ProspectBuilder deDateProspection(LocalDate date) {
        this.getEntity().setDateProspection(date);
        return this;
    }

    /**
     * Setter Date Prospection.
     *
     * @param date Nouvelle date.
     * @return This builder.
     */
    public ProspectBuilder deDateProspection(@NotNull Date date) {
        this.getEntity().setDateProspection(LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault()));
        return this;
    }

    /**
     * Setter Date Prospection.
     * (Filesystem / Mongo)
     *
     * @param date Nouvelle date.
     * @return This builder.
     */
    public ProspectBuilder deDateProspection(@NotNull String date) {
        String[] dt;
        LocalDate ldt;
        if(date.contains("-")) {
            dt = date.split("-");
            ldt= LocalDate.parse(dt[2] + '/' + dt[1] + '/' + dt[0], Formatters.FORMAT_DDMMYYYY);
        }else{
            ldt = LocalDate.parse(date, Formatters.FORMAT_DDMMYYYY);
        }
        return this.deDateProspection(ldt);
    }

    /**
     * Setter Prospect Interesse
     * @param interesse Nouvelle indication d'intéressement.
     * @return This builder.
     * @throws SocieteEntityException Exception set by prospectInteresse setter.
     */
    public ProspectBuilder dInteresse(String interesse) throws SocieteEntityException {
        this.getEntity().setProspectInteresse(interesse);
        return this;
    }

    /**
     * Getter Prospect construit.
     *
     * @return Prospect construit.
     */
    @Override
    public Prospect build() {
        return this.getEntity();
    }
}

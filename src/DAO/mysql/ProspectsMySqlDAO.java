package DAO.mysql;

import DAO.SocieteDatabaseException;
import entities.Prospect;
import entities.SocieteEntityException;
import logs.LogManager;
import org.jetbrains.annotations.NotNull;
import utilities.Formatters;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.logging.Level;

/**
 * Classe DAO pour les prospects.
 */
public class ProspectsMySqlDAO extends SocieteMySqlDAO<Prospect> {

    /**
     * Constructor
     */
    public ProspectsMySqlDAO() {
        super(new String[]{"raisonSociale", "commentaires"});
    }

    /**
     * Méthode qui retourne la table de la classe DAO.
     *
     * @return String
     */
    @Override
    protected String getTable() { return "prospects"; }

    /**
     * Méthode qui construit un Prospect à partir d'une ligne de résultats.
     *
     * @param rs Ligne de résultats
     * @return Prospect Le prospect récupéré.
     * @throws SocieteDatabaseException Exception lors de la récupération.
     */
    @Override
    protected Prospect parse(@NotNull ResultSet rs) throws SocieteDatabaseException {
        // Initialisation prospect.
        Prospect prospect = new Prospect();

        try {
            // Valorisation propriétés primitives.
            prospect.setIdentifiant(rs.getInt("identifiant"));
            prospect.setRaisonSociale(rs.getString("raisonSociale"));
            prospect.setTelephone(rs.getString("telephone"));
            prospect.setMail(rs.getString("mail"));
            prospect.setCommentaires(rs.getString("commentaires"));

            // Valorisation propriété date.
            String[] dt = rs.getString("dateProspection").split("-");
            LocalDate ldt = LocalDate.parse(dt[2] + '/' + dt[1] + '/' + dt[0], Formatters.FORMAT_DDMMYYYY);
            prospect.setDateProspection(ldt);

            // Valorisation propriétés booléennes.
            prospect.setProspectInteresse(rs.getInt("prospectInteresse") == 1 ? "oui" : "non");

            // Valorisation propriétés objets.
            prospect.setAdresse(MySqlFactory.getAdresseDAO().findById(rs.getInt("idAdresse")));
        } catch (SocieteEntityException | SQLException e) {
            // Log exception.
            LogManager.logs.log(Level.SEVERE, e.getMessage());

            // Lancement d'une exception lisible par l'utilisateur.
            throw new SocieteDatabaseException("Erreur de la récupération du " + "prospect depuis la base de données.");
        }

        // Retourne le prospect valorisé.
        return prospect;
    }

    /**
     * Méthode donnant les libellés de propriétés pour la classe Prospect.
     *
     * @return String[] Le tableau des libellés.
     */
    @Override
    protected String[] getTablePropertiesLabels() {
        return new String[]{"raisonSociale", "telephone", "mail", "commentaires", "dateProspection", "prospectInteresse", "idAdresse"};
    }

    /**
     * Méthode donnant les libellés de propriétés avec jetons pour la classe
     * Prospect.
     *
     * @return String[] le tableau des libellés avec jetons.
     */
    @Override
    protected String[] getTablePropertiesLabelsTokens() {
        return new String[]{"raisonSociale = ?", "telephone = ?", "mail = ?", "commentaires = ?", "dateProspection = ?", "prospectInteresse = ?", "idAdresse = ?"};
    }

    /**
     * Méthode pour lier les valeurs du prospect à la requête.
     *
     * @param obj  Le prospect.
     * @param stmt La requête préparée.
     * @throws SocieteDatabaseException Exception lors de la liaison.
     */
    @Override
    protected void bindTableProperties(@NotNull Prospect obj, PreparedStatement stmt) throws SocieteDatabaseException {
        try {
            // Valorisation de la requête avec les propriétés du prospect.
            stmt.setString(1, obj.getRaisonSociale());
            stmt.setString(2, obj.getTelephone());
            stmt.setString(3, obj.getMail());
            stmt.setString(4, obj.getCommentaires());
            stmt.setDate(5, Date.valueOf(obj.getDateProspection()));
            stmt.setInt(6, Objects.equals(obj.getProspectInteresse(), "oui") ? 1 : 0);
            stmt.setInt(7, obj.getAdresse().getIdentifiant());
        } catch (SQLException e) {
            // Log exception.
            LogManager.logs.log(Level.SEVERE, e.getMessage());

            // Lancement d'une exception lisible par l'utilisateur.
            throw new SocieteDatabaseException("Prospect n'arrive pas à être " +
                    "créé.");
        }
    }
}

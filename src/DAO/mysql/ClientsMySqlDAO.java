package DAO.mysql;

import DAO.SocieteDatabaseException;
import entities.Adresse;
import entities.Client;
import entities.SocieteEntityException;
import logs.LogManager;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * Classe DAO pour les clients.
 */
public class ClientsMySqlDAO extends SocieteMySqlDAO<Client> {

    /**
     * Constructor
     */
    public ClientsMySqlDAO() {
        super(new String[]{"raisonSociale", "commentaires"});
    }

    /**
     * Méthode qui retourne la table de la classe DAO.
     * @return String
     */
    @Override
    protected String getTable() {
        return "clients";
    }

    /**
     * Méthode qui construit un Client à partir d'une ligne de résultats.
     * @param rs Ligne de résultats
     * @return Client Le client récupéré.
     * @throws SocieteDatabaseException Exception lors de la récupération.
     */
    @Override
    protected Client parse(@NotNull ResultSet rs) throws SocieteDatabaseException {
        Client client = new Client();

        try {
            client.setIdentifiant(rs.getInt("identifiant"));
            client.setRaisonSociale(rs.getString("raisonSociale"));
            client.setTelephone(rs.getString("telephone"));
            client.setMail(rs.getString("mail"));
            client.setCommentaires(rs.getString("commentaires"));
            client.setChiffreAffaires(rs.getLong("chiffreAffaires"));
            client.setNbEmployes(rs.getInt("nbEmployes"));

            Adresse adresse = MySqlFactory.getAdresseDAO().findById(rs.getInt("idAdresse"));
            client.setAdresse(adresse);
        } catch (SocieteEntityException | SQLException e) {
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur de la récupération du " +
                    "client depuis la base de données.");
        }

        return client;
    }

    /**
     * Méthode donnant les libellés de propriétés pour la classe Client.
     * @return String[] Le tableau des libellés.
     */
    @Override
    protected String[] getTablePropertiesLabels() {
        return new String[]{"raisonSociale", "telephone", "mail", "commentaires", "chiffreAffaires", "nbEmployes", "idAdresse"};
    }

    /**
     * Méthode donnant les libellés de propriétés avec jetons pour la classe
     * Client.
     * @return String[] le tableau des libellés avec jetons.
     */
    @Override
    protected String[] getTablePropertiesLabelsTokens() {
        return new String[]{"raisonSociale = ?", "telephone = ?", "mail = ?",
                "commentaires = ?", "chiffreAffaires = ?", "nbEmployes = ?", "idAdresse = ?"};
    }

    /**
     * Méthode pour lier les valeurs du client à la requête.
     * @param obj Le client.
     * @param stmt La requête préparée.
     * @throws SocieteDatabaseException Exception lors de la liaison.
     */
    @Override
    protected void bindTableProperties(Client obj, PreparedStatement stmt) throws SocieteDatabaseException {
        try {
            stmt.setString(1, obj.getRaisonSociale());
            stmt.setString(2, obj.getTelephone());
            stmt.setString(3, obj.getMail());
            stmt.setString(4, obj.getCommentaires());
            stmt.setLong(5, obj.getChiffreAffaires());
            stmt.setInt(6, obj.getNbEmployes());
            stmt.setInt(7, obj.getAdresse().getIdentifiant());
        } catch (SQLException e) {
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Client n'arrive pas à être " +
                    "créé.");
        }
    }

    /**
     * Méthode pour affecter la valeur de la clé primaire du client avec les
     * données de la ligne de résultat.
     * @param obj Le client.
     * @param rs La ligne
     * @throws SocieteDatabaseException Exception lors de la récupération de
     * données depuis l'enregistrement ou exception lors de la valorisation
     * de la clé primaire.
     */
    @Override
    protected void setPrimaryKey(@NotNull Client obj, @NotNull ResultSet rs) throws SocieteDatabaseException {
        try {
            obj.setIdentifiant(rs.getInt(1));
        } catch (SocieteEntityException | SQLException e) {
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Le nouveau client n'est pas" +
                    " bien indexé.");
        }
    }
}

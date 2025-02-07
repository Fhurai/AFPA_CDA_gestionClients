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

    public ClientsMySqlDAO() {
        super(new String[] {"raisonSociale", "commentaires"});
    }

    /**
     * @return
     */
    @Override
    protected String getTable() {
        return "clients";
    }

    /**
     * @param rs
     * @return
     * @throws SocieteDatabaseException
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
     * @return
     */
    @Override
    protected String[] getTablePropertiesLabels() {
        return new String[]{"raisonSociale", "telephone", "mail", "commentaires", "chiffreAffaires", "nbEmployes","idAdresse"};
    }

    /**
     * @param obj
     * @param stmt
     * @throws SocieteDatabaseException
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
     * @param obj
     * @param rs
     * @throws SocieteDatabaseException
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

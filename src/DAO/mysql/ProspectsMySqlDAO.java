package DAO.mysql;

import DAO.SocieteDatabaseException;
import entities.Prospect;
import entities.SocieteEntityException;
import logs.LogManager;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class ProspectsMySqlDAO extends SocieteMySqlDAO<Prospect>{

    public ProspectsMySqlDAO() {
        super(new String[] {"raisonSociale", "commentaires"});
    }

    /**
     * @return
     */
    @Override
    protected String getTable() {
        return "prospects";
    }

    /**
     * @param rs
     * @return
     * @throws SocieteDatabaseException
     */
    @Override
    protected Prospect parse(ResultSet rs) throws SocieteDatabaseException {
        Prospect p = new Prospect();

        try {
            p.setIdentifiant(rs.getInt("identifiant"));
            p.setRaisonSociale(rs.getString("raisonSociale"));
            p.setTelephone(rs.getString("telephone"));
            p.setMail(rs.getString("mail"));
            p.setCommentaires(rs.getString("commentaires"));
            p.setDateProspection(rs.getDate("dateProspection").toLocalDate());
            p.setProspectInteresse(rs.getString("prospectInteresse"));
        } catch (SocieteEntityException | SQLException e) {
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur de la récupération du " +
                    "prospect depuis la base de données.");
        }

        return p;
    }

    /**
     * @return
     */
    @Override
    protected String[] getTablePropertiesLabels() {
        return new String[]{"raisonSociale", "telephone", "mail", "commentaires", "dateProspection", "prospectInteresse","idAdresse"};
    }

    /**
     * @param obj
     * @param stmt
     * @throws SocieteDatabaseException
     */
    @Override
    protected void bindTableProperties(Prospect obj, PreparedStatement stmt) throws SocieteDatabaseException {
        try {
            stmt.setString(1, obj.getRaisonSociale());
            stmt.setString(2, obj.getTelephone());
            stmt.setString(3, obj.getMail());
            stmt.setString(4, obj.getCommentaires());
            stmt.setDate(5, Date.valueOf(obj.getDateProspection()));
            stmt.setString(6, obj.getProspectInteresse());
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
    protected void setPrimaryKey(@NotNull Prospect obj, @NotNull ResultSet rs) throws SocieteDatabaseException {
        try {
            obj.setIdentifiant(rs.getInt(1));
        } catch (SocieteEntityException | SQLException e) {
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Le nouveau client n'est pas" +
                    " bien indexé.");
        }
    }
}

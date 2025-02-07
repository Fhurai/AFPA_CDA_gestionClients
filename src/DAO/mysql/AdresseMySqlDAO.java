package DAO.mysql;

import DAO.DAO;
import DAO.SocieteDatabaseException;
import entities.Adresse;
import entities.SocieteEntityException;
import logs.LogManager;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class AdresseMySqlDAO extends DAO<Adresse> {

    public AdresseMySqlDAO() {
        super(new String[]{"numRue", "nomRue", "codePostal", "ville"});
    }

    /**
     * @return
     */
    @Override
    protected String getTable() {
        return "adresses";
    }

    /**
     * @param rs
     * @return
     * @throws SocieteDatabaseException
     */
    @Override
    protected Adresse parse(@NotNull ResultSet rs) throws SocieteDatabaseException {
        Adresse adresse = new Adresse();

        try {
            adresse.setIdentifiant(rs.getInt("identifiant"));
            adresse.setNumeroRue(rs.getString("numRue"));
            adresse.setNomRue(rs.getString("nomRue"));
            adresse.setCodePostal(rs.getString("codePostal"));
            adresse.setVille(rs.getString("ville"));
        } catch (SocieteEntityException e) {
            throw new SocieteDatabaseException("Erreur de la récupération du " +
                    "client depuis la base de données.");
        } catch (SQLException e) {
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur de la récupération du " +
                    "client depuis la base de données.");
        }

        return adresse;
    }

    /**
     * @param obj
     * @return
     * @throws SocieteDatabaseException
     */
    @Override
    protected String[][] selectByPrimaryKey(@NotNull Adresse obj) throws SocieteDatabaseException {
        return new String[][] {{"identifiant", String.valueOf(obj.getIdentifiant())}};
    }

    /**
     * @param obj
     * @param stmt
     * @throws SocieteDatabaseException
     */
    @Override
    protected void bindPrimaryKey(@NotNull Adresse obj, @NotNull PreparedStatement stmt) throws SocieteDatabaseException {
        try {
            stmt.setInt(1, obj.getIdentifiant());
        } catch (SQLException e) {
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Adresse n'arrive pas à faire " +
                    "une sélection simple.");
        }
    }

    /**
     * @return
     */
    @Override
    protected String[] getTablePropertiesLabels() {
        return new String[]{"numRue", "nomRue", "codePostal", "ville"};
    }

    /**
     * @param obj
     * @param stmt
     */
    @Override
    protected void bindTableProperties(Adresse obj, PreparedStatement stmt) throws SocieteDatabaseException {
        try {
            stmt.setString(1, obj.getNumeroRue());
            stmt.setString(2, obj.getNomRue());
            stmt.setString(3, obj.getCodePostal());
            stmt.setString(4, obj.getVille());
        } catch (SQLException e) {
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Adresse n'arrive pas à être " +
                    "créé.");
        }
    }

    /**
     * @param obj
     * @param rs
     * @throws SocieteDatabaseException
     */
    @Override
    protected void setPrimaryKey(@NotNull Adresse obj, @NotNull ResultSet rs) throws SocieteDatabaseException {
        try {
            obj.setIdentifiant(rs.getInt(1));
        } catch (SocieteEntityException | SQLException e) {
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("La nouvelle adresse n'est pas" +
                    " bien indexée.");
        }
    }
}

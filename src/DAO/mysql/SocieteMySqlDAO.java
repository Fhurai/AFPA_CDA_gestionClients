package DAO.mysql;

import DAO.DAO;
import DAO.SocieteDatabaseException;
import entities.Client;
import entities.Societe;
import logs.LogManager;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

abstract public class SocieteMySqlDAO<T extends Societe> extends DAO<T> {

    public SocieteMySqlDAO(String[] likeProperties) {
        super(likeProperties);
    }

    /**
     * @param obj
     * @return
     * @throws SocieteDatabaseException
     */
    protected String[][] selectByPrimaryKey(@NotNull Client obj) throws SocieteDatabaseException {
        return new String[][]{{"identifiant", String.valueOf(obj.getIdentifiant())}};
    }

    /**
     * @param obj
     * @param stmt
     * @throws SocieteDatabaseException
     */
    protected void bindPrimaryKey(@NotNull Client obj,
                                  @NotNull PreparedStatement stmt,
                                  int nbParameters) throws SocieteDatabaseException {
        try {
            stmt.setInt(nbParameters, obj.getIdentifiant());
        } catch (SQLException e) {
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Société n'arrive pas à faire " +
                    "une sélection simple.");
        }
    }

    /**
     * Méthode pour rechercher un objet de type T à partir de sa raison sociale.
     *
     * @param raisonSociale La raison sociale de l'objet recherché.
     * @return Objet de type T recherché.
     * @throws SocieteDatabaseException
     */
    public T findByRaisonSociale(String raisonSociale) throws SocieteDatabaseException {
        // Initialisation de la condition de recherche.
        String[][] selection = {{"raisonSociale", raisonSociale}};

        // Retourne le client trouvé avec la condition de recherche.
        return this.find(selection);
    }

    @Override
    public boolean delete(T obj) throws SocieteDatabaseException {
        Connection conn = ConnexionMySql.getInstance();
        boolean ret = false;

        try {
            conn.setAutoCommit(false);
            ret = super.delete(obj);
            MySqlFactory.getAdresseDAO().delete(obj.getAdresse());
            conn.commit();
        } catch (SQLException e) {
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LogManager.logs.log(Level.SEVERE, e.getMessage());
                    throw new SocieteDatabaseException("Les données de " +
                            "sociétés ne sont plus intègres.");
                }
            }
            throw new SocieteDatabaseException("Problème lors de la " +
                    "suppression de la société.");
        }
        return ret;
    }

    @Override
    public boolean create(@NotNull T obj) throws SocieteDatabaseException {
        Connection conn = ConnexionMySql.getInstance();
        boolean ret = false;

        try {
            conn.setAutoCommit(false);
            MySqlFactory.getAdresseDAO().create(obj.getAdresse());
            ret = super.create(obj);
            conn.commit();
        } catch (SQLException e) {
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LogManager.logs.log(Level.SEVERE, e.getMessage());
                    throw new SocieteDatabaseException("Les données de " +
                            "sociétés ne sont plus intègres.");
                }
            }
            throw new SocieteDatabaseException("Problème lors de la " +
                    "suppression de la société.");
        }
        return ret;
    }

    public boolean update(T obj) throws SocieteDatabaseException {
        Connection con = ConnexionMySql.getInstance();
        boolean ret = false;

        try {
            con.setAutoCommit(false);
            ret = super.update(obj);
            MySqlFactory.getAdresseDAO().update(obj.getAdresse());
            con.commit();
        } catch (SQLException e) {
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    LogManager.logs.log(Level.SEVERE, e.getMessage());
                    throw new SocieteDatabaseException("Les données de " +
                            "sociétés ne sont plus intègres.");
                }
            }
            throw new SocieteDatabaseException("Problème lors de la " +
                    "modification de la société.");
        }

        // Retourne l'indication si la requête a modifié une et une seule
        // ligne d'enregistrement.
        return ret;
    }
}

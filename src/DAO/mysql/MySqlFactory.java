package DAO.mysql;

import DAO.DAOFactory;
import DAO.SocieteDatabaseException;
import logs.LogManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.logging.Level;

import static DAO.mysql.ConnexionMySql.getInstance;

/**
 * Classe factory pour les objets DAO MySql.
 */
public class MySqlFactory implements DAOFactory {

    /**
     * Méthode pour obtenir un objet DAO MySql pour adresse.
     *
     * @return objet DAO MySql pour adresse.
     */
    @Contract(" -> new")
    public static @NotNull AdresseMySqlDAO getAdresseDAO() {
        return new AdresseMySqlDAO();
    }

    @Override
    public void init() throws SocieteDatabaseException {
        getInstance();
    }

    @Override
    public void close() throws SocieteDatabaseException {
        try {
            ConnexionMySql.getInstance().close();
        } catch (SQLException e) {
            // Log de l'exception
            LogManager.logs.log(Level.SEVERE, e.getMessage());
            throw new SocieteDatabaseException("Erreur lors de la fermeture " +
                    "de la connexion");
        }
    }

    /**
     * Méthode pour obtenir un objet DAO MySql pour client.
     *
     * @return objet DAO MySql pour client.
     */
    @Contract(" -> new")
    public @NotNull ClientMySqlDAO getClientDAO() {
        return new ClientMySqlDAO();
    }

    /**
     * Méthode pour obtenir un objet DAO MySql pour prospect.
     *
     * @return objet DAO pour prospect.
     */
    @Contract(" -> new")
    public @NotNull ProspectMySqlDAO getProspectDAO() {
        return new ProspectMySqlDAO();
    }

    /**
     * Méthode pour obtenir un objet DAO MySql pour contrat.
     *
     * @return objet DAO pour contrat.
     */
    @Contract(" -> new")
    public static @NotNull ContratMySqlDAO getContratDAO() {
        return new ContratMySqlDAO();
    }
}

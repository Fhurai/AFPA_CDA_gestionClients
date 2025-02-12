package DAO.mysql;

import DAO.DAOFactory;
import DAO.SocieteDatabaseException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

/**
 * Classe factory pour les objets DAO MySql.
 */
public class MySqlFactory implements DAOFactory {

    public Connection getInstance() throws SocieteDatabaseException {
        return ConnexionMySql.getInstance();
    }

    /**
     * Méthode pour obtenir un objet DAO MySql pour adresse.
     * @return objet DAO MySql pour adresse.
     */
    @Contract(" -> new")
    public static @NotNull AdresseMySqlDAO getAdresseDAO() {
        return new AdresseMySqlDAO();
    }

    /**
     * Méthode pour obtenir un objet DAO MySql pour client.
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

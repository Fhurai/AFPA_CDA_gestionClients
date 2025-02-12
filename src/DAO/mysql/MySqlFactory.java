package DAO.mysql;

import DAO.DAOFactory;
import DAO.SocieteDatabaseException;
import logs.LogManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

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

    /**
     * Méthode pour obtenir un objet DAO MySql pour contrat.
     *
     * @return objet DAO pour contrat.
     */
    @Contract(" -> new")
    public static @NotNull ContratMySqlDAO getContratDAO() {
        return new ContratMySqlDAO();
    }

    /**
     * Méthode d'initialisation de l'usine et de la connexion DAO.
     *
     * @throws SocieteDatabaseException Exception lors de l'ouverture de la
     *                                  connexion.
     */
    @Override
    public void init() throws SocieteDatabaseException {
        ConnexionMySql.getInstance();
    }

    /**
     * Méthode de fermeture de la connexion.
     *
     * @throws SocieteDatabaseException Exception lors de la fermeture de la
     *                                  connexion.
     */
    @Override
    public void close() throws SocieteDatabaseException {
        // Log de la fermeture et fermeture de la connexion.
        LogManager.logs.log(Level.INFO, "Database fermée");
        ConnexionMySql.close();
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
     * @return objet DAO MySql pour prospect.
     */
    @Contract(" -> new")
    public @NotNull ProspectMySqlDAO getProspectDAO() {
        return new ProspectMySqlDAO();
    }
}

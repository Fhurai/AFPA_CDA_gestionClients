package DAO;

import entities.Client;
import entities.Prospect;

/**
 * Interface d'usine DAO.
 */
public interface DAOFactory {

    /**
     * Méthode d'initialisation de l'usine et de la connexion DAO.
     *
     * @throws SocieteDatabaseException Exception lors de l'ouverture de la
     *                                  connexion.
     */
    void init() throws SocieteDatabaseException;

    /**
     * Méthode de fermeture de la connexion.
     *
     * @throws SocieteDatabaseException Exception lors de la fermeture de la
     *                                  connexion.
     */
    void close() throws SocieteDatabaseException;

    /**
     * Getter classe DAO client.
     *
     * @return DAO client.
     */
    SocieteDAO<Client> getClientDAO();

    /**
     * Getter classe DAO prospect.
     *
     * @return DAO prospect.
     */
    SocieteDAO<Prospect> getProspectDAO();
}

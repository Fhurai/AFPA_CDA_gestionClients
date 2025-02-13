package DAO.mongo;

import DAO.DAOFactory;
import DAO.SocieteDAO;
import DAO.SocieteDatabaseException;
import entities.Client;
import entities.Prospect;
import logs.LogManager;

import java.util.logging.Level;

import static DAO.mongo.ConnexionMongo.getInstance;

/**
 * Classe factory pour les objets DAO MongoDB.
 */
public class MongoFactory implements DAOFactory {

    /**
     * Méthode d'initialisation de l'usine et de la connexion DAO.
     *
     * @throws SocieteDatabaseException Exception lors de l'ouverture de la
     *                                  connexion.
     */
    @Override
    public void init() throws SocieteDatabaseException {
        getInstance();
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
        ConnexionMongo.close();
    }

    /**
     * Méthode pour obtenir un objet DAO MongoDB pour client.
     *
     * @return objet DAO MongoDB pour client.
     */
    @Override
    public SocieteDAO<Client> getClientDAO() {
        return new ClientMongoDAO();
    }

    /**
     * Méthode pour obtenir un objet DAO MongoDB pour prospect.
     *
     * @return objet DAO MongoDB pour prospect.
     */
    @Override
    public SocieteDAO<Prospect> getProspectDAO() {
        return new ProspectMongoDAO();
    }
}

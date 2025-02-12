package DAO.mongo;

import DAO.DAOFactory;
import DAO.SocieteDAO;
import DAO.SocieteDatabaseException;
import entities.Client;
import entities.Prospect;

import static DAO.mongo.ConnexionMongo.getInstance;

public class MongoFactory implements DAOFactory {

    @Override
    public void init() throws SocieteDatabaseException {
        getInstance();
    }

    @Override
    public void close() throws SocieteDatabaseException {
        ConnexionMongo.close();
    }

    @Override
    public SocieteDAO<Client> getClientDAO() {
        return new ClientMongoDAO();
    }

    @Override
    public SocieteDAO<Prospect> getProspectDAO() {
        return new ProspectMongoDAO();
    }
}

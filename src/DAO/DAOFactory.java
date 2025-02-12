package DAO;

import entities.Client;
import entities.Prospect;

public interface DAOFactory {

    void init() throws SocieteDatabaseException;

    void close() throws SocieteDatabaseException;

    SocieteDAO<Client> getClientDAO();

    SocieteDAO<Prospect> getProspectDAO();
}
